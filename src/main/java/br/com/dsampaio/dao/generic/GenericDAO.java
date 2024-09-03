package br.com.dsampaio.dao.generic;

import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.dsampaio.dao.generic.jdbc.ConnectionFactory;
import br.com.dsampaio.annotation.ColunaTabela;
import br.com.dsampaio.annotation.Tabela;
import br.com.dsampaio.annotation.TipoChave;
import br.com.dsampaio.dao.Persistente;
import br.com.dsampaio.exceptions.*;

public abstract class GenericDAO<T extends Persistente, E extends Serializable> implements IGenericDAO<T, E> {

    public abstract Class<T> getTipoClasse();

    public abstract void atualizarDados(T entity, T entityCadastrado);

    protected abstract String getQueryInsercao();

    protected abstract String getQueryExclusao();

    protected abstract String getQueryAtualizacao();

    protected abstract void setParametrosQueryInsercao(PreparedStatement stmInsert, T entity) throws SQLException;

    protected abstract void setParametrosQueryExclusao(PreparedStatement stmExclusao, E valor) throws SQLException;

    protected abstract void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, T entity) throws SQLException;

    protected abstract void setParametrosQuerySelect(PreparedStatement stmSelect, E valor) throws SQLException;

    protected Connection getConnection() throws ExceptionDao {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new ExceptionDao("Erro ao abrir conexão com o banco de dados", e);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public E getChave(T entity) throws ExceptionTipoChaveNaoEncontrada {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TipoChave.class)) {
                TipoChave tipoChave = field.getAnnotation(TipoChave.class);
                String nameMetodo = tipoChave.value();
                try {
                    Method method = entity.getClass().getMethod(nameMetodo);
                    return (E) method.invoke(entity);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new ExceptionTipoChaveNaoEncontrada(
                            "Chave principal do objeto " + entity.getClass().getName() + " não encontrada", e);
                }
            }
        }
        throw new ExceptionTipoChaveNaoEncontrada("Chave principal do objeto " + entity.getClass().getName() + " não encontrada");
    }

    public Boolean cadastrar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(getQueryInsercao(), Statement.RETURN_GENERATED_KEYS)) {
            setParametrosQueryInsercao(stm, entity);
            int linhaAfetada = stm.executeUpdate();
            if (linhaAfetada > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        Persistente per = (Persistente) entity;
                        per.setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            throw new ExceptionDao("Erro ao cadastrar objeto", e);
        }
        return false;
    }

    public void excluir(E valor) throws ExceptionDao {
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(getQueryExclusao())) {
            setParametrosQueryExclusao(stm, valor);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new ExceptionDao("Erro ao excluir objeto", e);
        }
    }

    public void alterar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(getQueryAtualizacao())) {
            setParametrosQueryAtualizacao(stm, entity);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new ExceptionDao("Erro ao alterar objeto", e);
        }
    }

    public T consultar(E valor) throws ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTipoChaveNaoEncontrada {
        try {
            validarMaisDeUmRegistro(valor);
            try (Connection connection = getConnection();
                 PreparedStatement stm = connection.prepareStatement(
                         "SELECT * FROM " + getTableName() + " WHERE " + getNomeCampoChave(getTipoClasse()) + " = ?")) {
                setParametrosQuerySelect(stm, valor);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        T entity = getTipoClasse().getConstructor().newInstance();
                        preencherEntidade(rs, entity);
                        return entity;
                    }
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new ExceptionDao("Erro ao consultar objeto", e);
        }
        return null;
    }

    public Collection<T> buscarTodos() throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTable {
        List<T> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT * FROM " + getTableName());
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                T entity = getTipoClasse().getConstructor().newInstance();
                preencherEntidade(rs, entity);
                list.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new ExceptionDao("Erro ao listar objetos", e);
        }
        return list;
    }

    private Long validarMaisDeUmRegistro(E valor) throws ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(
                     "SELECT count(*) FROM " + getTableName() + " WHERE " + getNomeCampoChave(getTipoClasse()) + " = ?")) {
            setParametrosQuerySelect(stm, valor);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Long count = rs.getLong(1);
                    if (count > 1) {
                        throw new ExceptionMaisDeUmRegistro("Encontrado mais de um registro na tabela " + getTableName());
                    }
                    return count;
                }
            }
        } catch (SQLException e) {
            throw new ExceptionDao("Erro ao validar registros", e);
        }
        return null;
    }

    private String getTableName() throws ExceptionTable {
        if (getTipoClasse().isAnnotationPresent(Tabela.class)) {
            Tabela table = getTipoClasse().getAnnotation(Tabela.class);
            return table.value();
        } else {
            throw new ExceptionTable("Tabela não encontrada para o tipo " + getTipoClasse().getName());
        }
    }

    private String getNomeCampoChave(Class<?> clazz) throws ExceptionTipoChaveNaoEncontrada {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TipoChave.class) && field.isAnnotationPresent(ColunaTabela.class)) {
                ColunaTabela coluna = field.getAnnotation(ColunaTabela.class);
                return coluna.dbName();
            }
        }
        throw new ExceptionTipoChaveNaoEncontrada("Campo chave não encontrado para o tipo " + clazz.getName());
    }

    private void preencherEntidade(ResultSet rs, T entity) throws SQLException, ExceptionElementoNaoConhecido, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ColunaTabela.class)) {
                ColunaTabela coluna = field.getAnnotation(ColunaTabela.class);
                String dbName = coluna.dbName();
                String javaSetName = coluna.setJavaName();
                Class<?> classField = field.getType();
                Method method = entity.getClass().getMethod(javaSetName, classField);
                setValueByType(entity, method, classField, rs, dbName);
            }
        }
    }

    private void setValueByType(T entity, Method method, Class<?> classField, ResultSet rs, String fieldName)
            throws IllegalAccessException, InvocationTargetException, SQLException, ExceptionElementoNaoConhecido {

        if (classField.equals(Integer.class)) {
            Integer val = rs.getInt(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Long.class)) {
            Long val = rs.getLong(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Double.class)) {
            Double val = rs.getDouble(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Short.class)) {
            Short val = rs.getShort(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(BigDecimal.class)) {
            BigDecimal val = rs.getBigDecimal(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(String.class)) {
            String val = rs.getString(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(LocalDateTime.class)) {
            Timestamp timestamp = rs.getTimestamp(fieldName);
            LocalDateTime val = timestamp != null ? timestamp.toLocalDateTime() : null;
            method.invoke(entity, val);
        } else {
            throw new ExceptionElementoNaoConhecido("Tipo de classe não conhecido: " + classField);
        }
    }
}

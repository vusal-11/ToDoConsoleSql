package services;

import entity.Todo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TodoService {

    public void createTable(Connection connection) {

        var sql = """
                CREATE TABLE IF NOT EXISTS todo(
                id SERIAL PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                is_done BOOLEAN NOT NULL,
                is_deleted BOOLEAN NOT NULL
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void insert(Connection connection, String title, String description) {

        var sql = """
                INSERT INTO todo(title,description,is_done,is_deleted) VALUES(?,?,false,false);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public List<Todo> findAll(Connection connection) {
        var sql = """
                SELECT * FROM todo WHERE is_deleted=false;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            List<Todo> todos = new ArrayList<>();
            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setDescription(resultSet.getString("description"));
                todo.setDone(resultSet.getBoolean("is_done"));
                todos.add(todo);
            }
            return todos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void delete(Connection connection, int id) {
        var sql = """
            UPDATE todo SET is_deleted = true WHERE id=? AND is_deleted = false
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No Todo found with ID " + id + " or it's already deleted.");
            } else {
                System.out.println("Todo with ID " + id + " deleted successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Connection connection, Todo todo) {
        var sql = "UPDATE todo SET title=?, description=?, is_done=? WHERE id=? AND is_deleted=false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, todo.getDescription());
            preparedStatement.setBoolean(3, todo.isDone());
            preparedStatement.setInt(4, todo.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No Todo found with ID " + todo.getId() + " or it's already deleted.");
            } else {
                System.out.println("Todo with ID " + todo.getId() + " updated successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Todo findById(Connection connection, int id) {
        var sql = "SELECT * FROM todo WHERE id=? AND is_deleted=false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setDescription(resultSet.getString("description"));
                todo.setDone(resultSet.getBoolean("is_done"));
                return todo;
            } else {
                System.out.println("No Todo found with ID " + id + " or it's already deleted.");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

package io.zensoft.repository;

import io.zensoft.model.TelegramUser;
import io.zensoft.util.DatabaseManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class DatabaseManager {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseManager(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserExist(Long userId) {
        try {
            jdbcTemplate.queryForObject(DatabaseManagerUtil.IS_USER_EXIST, Long.class, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public boolean saveTelegramUser(TelegramUser telegramUser) {
        Object[] arguments = {telegramUser.getId(), telegramUser.getFirstName(), telegramUser.getLastName(), telegramUser.getUserName()};
        int[] types = {Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        int update = jdbcTemplate.update("INSERT INTO telegram_user (id, first_name, last_name, user_name) VALUES (?, ?, ?, ?)", arguments, types);
        return update > 0;
    }
}

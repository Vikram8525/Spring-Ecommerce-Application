package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.SellerDetails;

public class SellerDetailsMapper implements RowMapper<SellerDetails>{

	@Override
    public SellerDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        SellerDetails details = new SellerDetails();
        details.sellerName = rs.getString("user_name");
        details.sellerState = rs.getString("state");
        details.sellerCity = rs.getString("city");
        return details;
    }
}

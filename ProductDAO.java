package pro;

import java.sql.*;
import java.util.*;

public class ProductDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost/abc?useSSL=false";
	Connection conn;
	
	PreparedStatement pstmt;
	ResultSet rs;
	
	// 콤보박스 아이템 관리를 위한 벡터
	Vector<String> items = null;
	String sql;
    
	 public void connectDB() {
     	try {
     		Class.forName(jdbcDriver); // JDBC 드라이버 로드
     		
     		conn = DriverManager.getConnection(jdbcUrl, "root", "0000"); // DB연결
     	} catch (Exception e) {
     		e.printStackTrace();
     	}
     }
     public void closeDB() {
     	try {
     		pstmt.close();
     		rs.close();
     		conn.close();
     	} catch (SQLException e) {
     		e.printStackTrace();
     	}
     }
	public ArrayList<Product> getAll(){
		connectDB();
		sql = "select * from product";
		
		// 전체 검색 데이터를 전달하는  ArrayList
		ArrayList<Product> datas = new ArrayList<Product>();
		
		// 관리번호 콤보박스 데이터를 위한 벡터 초기화
		items = new Vector<String>();
		items.add("전체");
		
		try {
			pstmt = conn.prepareStatement(sql);
  
    		rs = pstmt.executeQuery();
			while(rs.next()) {
				Product p = new Product();
				p.setPrcode(rs.getInt("prcode"));
				p.setPrname(rs.getString("prname"));
				p.setPrice(rs.getInt("price"));
				p.setManufacture(rs.getString("manufacture"));
				datas.add(p);
				items.add(String.valueOf(rs.getInt("prcode")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;		
	}
	public Product getProduct(int prcode) {
		connectDB();
		sql = "select * from product where prcode = ?";
		Product p = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			rs = pstmt.executeQuery();
			
    		rs.next();
    		p = new Product();
    		p.setPrcode(rs.getInt("prcode"));
    		p.setPrname(rs.getString("prname"));
    		p.setPrice(rs.getInt("price"));
    		p.setManufacture(rs.getString("manufacture"));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	public boolean newProduct(Product product) {
		connectDB();
		sql = "insert into product (prname, price, manufacture) values(?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product.getPrname());
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getManufacture());
    		pstmt.executeUpdate();	// SQL문 전송
    		return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean delProduct(int prcode) {
		connectDB();
		sql = "delete from product where prcode = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
    		pstmt.executeUpdate();	// SQL문 전송
    		return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateProduct(Product product) {
		connectDB();
		sql = "update product set prname = ?, price = ?, manufacture = ? where prcode = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product.getPrname());
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getManufacture());
			pstmt.setInt(4, product.getPrcode());
    		pstmt.executeUpdate();	// SQL문 전송
    		return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public Vector<String> getItem(){
		return items;
	}
	
}

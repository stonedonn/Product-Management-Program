package pro;

import java.sql.*;
import java.util.*;

public class ProductDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost/abc?useSSL=false";
	Connection conn;
	
	PreparedStatement pstmt;
	ResultSet rs;
	
	// �޺��ڽ� ������ ������ ���� ����
	Vector<String> items = null;
	String sql;
    
	 public void connectDB() {
     	try {
     		Class.forName(jdbcDriver); // JDBC ����̹� �ε�
     		
     		conn = DriverManager.getConnection(jdbcUrl, "root", "0000"); // DB����
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
		
		// ��ü �˻� �����͸� �����ϴ�  ArrayList
		ArrayList<Product> datas = new ArrayList<Product>();
		
		// ������ȣ �޺��ڽ� �����͸� ���� ���� �ʱ�ȭ
		items = new Vector<String>();
		items.add("��ü");
		
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
    		pstmt.executeUpdate();	// SQL�� ����
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
    		pstmt.executeUpdate();	// SQL�� ����
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
    		pstmt.executeUpdate();	// SQL�� ����
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

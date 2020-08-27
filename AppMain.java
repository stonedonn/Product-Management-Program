package pro;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppMain {
	static class startUI extends JFrame{
		private ProductDAO dao;
		private Product product;
		
		ArrayList<Product> datas = new ArrayList<Product>();
		boolean editmode;
		
		protected JLabel ml, l1, l2, l3, l4;
		protected JPanel p1, p2, p3;
		protected JTextArea ta, t1, t2, t3;
		protected JScrollPane scroll;
		protected JComboBox cb;
		protected JButton b1, b2, b3;
		
		private BtnListener BtnL;
		
		public startUI(){
			setName("Product Manager Application");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
			
			BtnL = new BtnListener();
			
			b1 = new JButton("등록");
			b2 = new JButton("조회");
			b3 = new JButton("삭제");
			
			b1.addActionListener(BtnL);
			b2.addActionListener(BtnL);
			b3.addActionListener(BtnL);

			ml = new JLabel("##메시지: 프로그램이 시작되었습니다!!");
			p1 = new JPanel(new GridLayout(4, 1, 0, 10));
			p2 = new JPanel(new GridLayout(4, 1, 0, 10));
			p3 = new JPanel();
			
			cb = new JComboBox();
			cb.addActionListener(BtnL);
			//cb.addItem("전체");
			ta = new JTextArea(10,40);
			scroll = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

			l1 = new JLabel("관리번호");
			l2 = new JLabel("상품명");
			l3 = new JLabel("단 가");
			l4 = new JLabel("제조사");
			p1.add(l1);
			p1.add(l2);
			p1.add(l3);
			p1.add(l4);

			t1 = new JTextArea();
			t2 = new JTextArea();
			t3 = new JTextArea();
			p2.add(cb);
			p2.add(t1);
			p2.add(t2);
			p2.add(t3);

			p3.add(b1);
			p3.add(b2);
			p3.add(b3);
			
			add(ml, BorderLayout.PAGE_START);
			add(p1, BorderLayout.LINE_START);
			add(p2, BorderLayout.CENTER);
			add(scroll, BorderLayout.LINE_END);
			add(p3, BorderLayout.PAGE_END);
			
			//ta.setFont(new Font("돋음", Font.PLAIN, 12));
			//add(textArea);
			this.setTitle("Product Manager Application");
			setSize(700, 300);
			setVisible(true);
			refreshData();
		}

		public void refreshData() {
			dao = new ProductDAO();
			ta.setText("");
			clearField();
			editmode = false;
			
			ta.append("관리번호\t상품명\t\t단가\t제조사\n");
			datas = dao.getAll();
			
			//데이터를 변경하면 콤보박스 데이터 갱신
			cb.setModel(new DefaultComboBoxModel(dao.getItem()));
			if(datas != null) {
				// ArrayList의 전체 데이터를 형식에 맞춰 출력
				for(Product p : datas) {
					StringBuffer sb = new StringBuffer();
					sb.append(p.getPrcode() + "\t");
					sb.append(p.getPrname() + "\t\t");
					sb.append(p.getPrice() + "\t");
					sb.append(p.getManufacture() + "\n");
					ta.append(sb.toString());
				}
			}
			else{
				ta.append("등록된 상품이 없습니다. !!\n상품을 등록해 주세요 !!");
			}
		}
		public void clearField() {
			t1.setText("");
			t2.setText("");
			t3.setText("");
		}
		private class BtnListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				dao = new ProductDAO();
				String msg = "##메시지: ";
				Object obj = e.getSource();
				if(obj == b1) {
					product = new Product();
					product.setPrname(t1.getText());
					product.setPrice(Integer.parseInt(t2.getText()));
					product.setManufacture(t3.getText());
						
					// 수정일 때
					if(editmode == true) {
						product.setPrcode(Integer.parseInt((String)cb.getSelectedItem()));
						if(dao.updateProduct(product)) {
							ml.setText(msg + "상품을 수정했습니다!!");
							clearField();
							editmode = false;
						}
						else
							ml.setText(msg + "상품 수정이 실패했습니다!!");
					}
					// 등록일 때
					else {
						if(dao.newProduct(product)) {
							ml.setText(msg + "상품을 등록했습니다!!");
						}
						else
							ml.setText(msg + "상품 등록이 실패했습니다!!");
					}
					// 화면 데이터 갱신
					refreshData();
				}
				else if(obj == b2) {
					product = new Product();
					String s = (String)cb.getSelectedItem();
					if(!s.equals("전체")) {
						product = dao.getProduct(Integer.parseInt(s));
						if(product != null) {
							ml.setText(msg + "상품정보를 가져왔습니다!!");
							t1.setText(product.getPrname());
							t2.setText(String.valueOf(product.getPrice()));
							t3.setText(product.getManufacture());
							// cb.setSelectedIndex(anIndex);
							editmode = true;
						}
						else {
							ml.setText(msg + "상품이 검색되지 않았습니다!!");
						}
					}
					else {clearField();}
				}
				else if(obj == b3) {
					String s = (String)cb.getSelectedItem();
					if(s.equals("전체")) {
						ml.setText(msg + "전체 삭제는 되지 않습니다!!");
					}
					else {
						if(dao.delProduct(Integer.parseInt(s))) {
							ml.setText(msg + "상품이 삭제되었습니다!!");
						}
						else {
							ml.setText(msg + "상품이 삭제되지 않았습니다!!");
						}
					}
					refreshData();
				}
				else if(obj == cb) {
					JComboBox<String> cc = (JComboBox<String>)e.getSource();
					int index = cc.getSelectedIndex();
					//ml.setText(index+" =");
				}
			}
		}	
	}	
	
	public static void main(String[] args) {
		new startUI();
	}

}

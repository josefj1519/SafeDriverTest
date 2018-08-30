import javax.swing.JFrame;

public class sdFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame j1 = new JFrame();
		j1.setTitle("Safe Driver Test");
		j1.setSize(800, 600);
		j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j1.setLocationRelativeTo(null);
		
		j1.add(new sdtComp());
		
		j1.setVisible(true);

	}

}

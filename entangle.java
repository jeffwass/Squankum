import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.lang.Math.*;
import java.util.Random;

public class entangle extends JApplet {

    public tangleFrame fr;
	
    public void init() {
	setName ("Entangle-JApplet");
	fr = new tangleFrame();
	//fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void start() {
	fr.tangleInit();
    }

    public void stop() {
    }

    public void destroy() {
    }
}

class tangleFrame extends JFrame {

    tangPanel panDraw;
    JPanel panCont;
    Container cont, boxCont, nSpinCont, totalSpinCont;
    JLabel nSpinLab,totalSpinLab;
    JButton spin1But,spin2But;
    JButton singletBut,tripletBut;
    JButton measBut;
    public TableModel tabModel;
    public JTable table;

    public tangleFrame (){

	super("Quantum Entanglement of Two Spins");
	panDraw = new tangPanel();
	panCont = new JPanel();

	tabModel = new tangTableModel();
	table = new JTable (tabModel);

	spin1But = new JButton ("One Spin");
	spin2But = new JButton ("Two Spins");
	singletBut = new JButton ("Singlet");
	tripletBut = new JButton ("Triplet");

	nSpinCont = Box.createHorizontalBox();
	nSpinCont.add(Box.createHorizontalGlue());
	nSpinCont.add(spin1But);
	nSpinCont.add(spin2But);
	nSpinCont.add(Box.createHorizontalGlue());

	totalSpinCont = Box.createHorizontalBox();
	totalSpinCont.add(Box.createHorizontalGlue());
	totalSpinCont.add(singletBut);
	totalSpinCont.add(tripletBut);
	totalSpinCont.add(Box.createHorizontalGlue());

	boxCont = Box.createVerticalBox();
	boxCont.add(new JLabel("Number of Spins in System"));
	boxCont.add(nSpinCont); 
	boxCont.add(new JLabel("Entangle Spins"));
	boxCont.add(totalSpinCont);
	measBut = new JButton ("Measure");
	boxCont.add(measBut);
	boxCont.add(table);
	boxCont.add(new JLabel("Demonstration part of :"));
	boxCont.add(new JLabel("Virtual Quantum Mechanics"));
	boxCont.add(new JLabel("Johns Hopkins University"));
	boxCont.add(new JLabel("CER TF 2003-2004"));
	boxCont.add(new JLabel("www.pha.jhu.edu/~javalab"));

	cont = getContentPane();
	cont.setLayout(new BorderLayout());
	cont.add(panDraw, BorderLayout.WEST);
	cont.add(boxCont, BorderLayout.EAST);

	pack();
	setVisible(true);

	// Handle Buttons
	measBut.addActionListener (new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panDraw.measureAction();
		}
	    });
	spin1But.addActionListener (new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panDraw.numberOfSpins=1;
		    panDraw.singlet=false;
		    panDraw.spinInit();
		}
	    });
	spin2But.addActionListener (new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panDraw.numberOfSpins=2;
		    panDraw.singlet=false;
		    panDraw.spinInit();
		}
	    });
	singletBut.addActionListener (new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panDraw.numberOfSpins=2;
		    panDraw.singlet=true;
		    panDraw.spinInit();
		}
	    });	

    }

    public void tangleInit () {
	panDraw.numberOfSpins=1;
	panDraw.spinInit();
	panDraw.paramUpdate();
	panDraw.repaint();
    }

    class tangTableModel extends AbstractTableModel {
	private String[] headings = new String[] {
	    "Field", "Spin1","Spin2"};
	private Object[][] data = new Object[][] {
	    {"Parameter","Spin 1","Spin 2"},
	    {"Spin Angle", " ", " "},
	    {"Measure Angle", " "," "},
	    {"Expection Prob."," "," "}
	};

	public int getRowCount() {return data.length;}
	public int getColumnCount() {return data[0].length;}
	public Object getValueAt(int row,
				 int column) {
	    return data[row][column];}
	public String getColumnName (int column) {
	    return headings[column];
	}
	public Class getColumnClass(int column) {
	    return data[0][column].getClass();
	}
	public void setValueAt (Object value,
				int row,
				int column) {
	    data[row][column] = value;
	    fireTableDataChanged();
	}
    }

}

class tangPanel extends JPanel 
    implements MouseMotionListener, MouseListener {

    public float bord=10.0f;
    public float axSpace=50.0f;
    public float fontSpace=20.0f;
    public int fontSize=12;
    public int panXSize;
    public int panYSize;

    public float spinLength=100.0f;
    public float measDirLength=50.0f;
    public float oldSpinLength=75.0f;
    public float measDirSep=2.0f;
    float spinArrowLength=15.0f;
    float spinArrowAngle=(float)(Math.PI/8.0);
    float angleResolution=(float)(Math.PI*3.0f/180.0f);
    float hiLiteRange=8.0f;

    public float[] spinAngle = {0,0};
    public float[] measAngle = {0,0};
    public float[] oldSpinAngle = {0,0};
    float[] centerX = {0,0};
    float[] centerY = {0,0};
    public int angRef=1;
    public int numberOfSpins=1;
    public boolean singlet;
    public boolean showOldSpin;

    public boolean[] hiSpinAngle = {false,false};
    public boolean[] hiMeasAngle = {false,false};
    public boolean[] moveSpinAngle = {false,false};
    public boolean[] moveMeasAngle = {false,false};

    Color backCol = new Color (0,0,0);
    Color spinCol = new Color (0xDD,0xDD,0);
    Color spinHiCol = new Color (0xFF,0xFF,0);
    Color measDirCol = new Color (0,0xDD,0);
    Color measDirHiCol = new Color (0,0xFF,0);
    Color oldSpinCol = new Color (0x77,0x77,0x77);
    Color circCol = new Color (0xAA,0xAA,0xAA);
    Color ringCol = new Color (0xAA,0xAA,0);
    Color axesCol = new Color (0xFF,0xFF,0xFF);
    Color fontCol = new Color (0xFF,0xFF,0xFF);
    Color statCol = new Color (0xFF,0x33,0x33);

    public tangPanel () {
	panXSize=Math.round(4.0f*bord+fontSpace+axSpace+4.0f*spinLength);
	panYSize=Math.round(3.0f*bord+fontSpace+2.0f*spinLength);
	setPreferredSize (new Dimension(panXSize,panYSize));
	addMouseListener(this);
	addMouseMotionListener(this);
    }

    public void measureAction () {
	int n;

	showOldSpin = true;
	for (n=0;n<numberOfSpins;n++) {
	    oldSpinAngle[n] = spinAngle[n];
	    if ((Math.pow(Math.cos((spinAngle[n]-measAngle[n])/2.0f),2))
		>= Math.random()) 
		spinAngle[n] = measAngle[n];
	    else
		spinAngle[n] = measAngle[n] + (float) Math.PI;
	}
	if (singlet) {
	    spinAngle[0] = measAngle[0];
	    spinAngle[1] = measAngle[1] + (float) Math.PI;
	    if (Math.random() > 0.5) {
		spinAngle[0] += (float) Math.PI;
		spinAngle[1] += (float) Math.PI;
	    }
	    singlet = false;
	    showOldSpin = false;
	}
	paramUpdate();
	repaint();
    }

    public void spinInit () {
	int n;

	showOldSpin=false;
	for (n=0; n<numberOfSpins; n++) {
	    spinAngle[n]=0f;
	    measAngle[n]=(float) Math.PI/2.0f;
	}
	if (numberOfSpins == 1)
	    centerX[0] = axSpace + (2.5f*bord) + 2.0f*spinLength;
	else {
	    centerX[0] = axSpace + 2.0f*bord + spinLength;
	    centerX[1] = axSpace + 3.0f*bord + 3.0f*spinLength;
	}
	centerY[0] = bord + spinLength;
	centerY[1] = centerY[0];
	paramUpdate();
	repaint();
    }

    public void paramUpdate () {
	tangleFrame dummy = (tangleFrame) getTopLevelAncestor();
	int i,j,n;
	float mathDummy;
	for (i=1;i<=3;i++)
	    for (j=1;j<=2;j++)
		dummy.table.setValueAt (new String(" "),i,j);
	for (n=0; n<numberOfSpins;n++) {
	    mathDummy = spinAngle[n]/(float)Math.PI*180f;
	    if (mathDummy < 0f)
		mathDummy += 360f;
	    mathDummy %= 360f;
	    dummy.table.setValueAt (new String(""+mathDummy),1,n+1);
	    if (singlet)
		dummy.table.setValueAt (new String("?"),1,n+1);
	    mathDummy = measAngle[n]/(float)Math.PI*180f;
	    if (mathDummy < 0f)
		mathDummy += 360f;
	    mathDummy %= 360f;
	    if (mathDummy > 180f) 
		mathDummy -= 180f;
	    dummy.table.setValueAt (new String(""+mathDummy),2,n+1);
	    mathDummy = (float) Math.pow(Math.cos
					 ((spinAngle[n]-measAngle[n])/2.0f),2.0f);
	    if (mathDummy < 0.5f)
		mathDummy = (1.0f - mathDummy);
	    dummy.table.setValueAt (new String(""+mathDummy),3,n+1);
	    if (singlet)
		dummy.table.setValueAt (new String(" "),3,n+1);
	}
    }
	    	
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
	int n;
	for (n=0; n<numberOfSpins; n++) {
	    if (hiSpinAngle[n]) moveSpinAngle[n]=true;
	    if (hiMeasAngle[n]) moveMeasAngle[n]=true;
	}
    }

    public void mouseReleased(MouseEvent e) {
	int n;
	for (n=0; n<numberOfSpins; n++) {
	    moveSpinAngle[n]=false;
	    moveMeasAngle[n]=false;
	}
	repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
	int n;
	for (n=0; n<numberOfSpins; n++) {
	    if (moveSpinAngle[n])
		spinAngle[n] = scrToAng(e.getX(),e.getY(),n);
	    if (moveMeasAngle[n]) {
		float ang = scrToAng(e.getX(),e.getY(),n);
		measAngle[n] = ang;
		if (singlet) {
		    measAngle[0] = ang;
		    measAngle[1] = ang;
		}   
	    }
	}
	paramUpdate();
	repaint();
    }

    public void mouseMoved(MouseEvent e) {
	int n;
	int x=e.getX();
	int y=e.getY();
	for (n=0; n<numberOfSpins; n++) {
	    hiSpinAngle[n]=false;
	    hiMeasAngle[n]=false;
	    if (inRange(x,y,angToScrX(spinAngle[n],spinLength,n),
			angToScrY(spinAngle[n],spinLength,n),hiLiteRange)) {
		hiSpinAngle[n]=true;
	    }
	    if (inRange(x,y,angToScrX(measAngle[n],measDirLength,n),
			angToScrY(measAngle[n],measDirLength,n),hiLiteRange)) {
		hiMeasAngle[n]=true;
	    }
	    if (inRange(x,y,angToScrX(measAngle[n]+(float)Math.PI,measDirLength,n),
			angToScrY(measAngle[n]+(float)Math.PI,measDirLength,n),hiLiteRange)) {
		hiMeasAngle[n]=true;
	    }
	}
	repaint();
    }

    private boolean inRange (float xx,
			     float yy,
			     float xgoal,
			     float ygoal,
			     float r) {
	boolean val=false;
	double f;

	if ((Math.pow(xx-xgoal,2) + Math.pow(yy-ygoal,2))
	    <= Math.pow(r,2)) val=true;
	return (val);
    }

    public int angToScrX (float angle,
			  float length,
			  int ref) {

	return ((int)Math.round(centerX[ref]+length*Math.sin(angle)));
    }

    public int angToScrY (float angle,
			  float length,
			  int ref) {
	return ((int)Math.round(centerY[ref]-length*Math.cos(angle)));
    }

    public float scrToAng (int x,
			   int y,
			   int ref) {
	float effX, effY;
	effX=x-centerX[ref];
	effY=y-centerY[ref];
	double ang=Math.atan(-effX/effY);
	if (effY >= 0) ang+=Math.PI;
	ang = angleResolution * Math.round(ang/angleResolution);
	return((float)ang);
    }

    public void drawArrow (Graphics2D g,
			   Color col,
			   double centX,
			   double centY,
			   double len,
			   double ang,
			   double arrLen,
			   double arrAng) {
	
	g.setPaint (col);
	g.drawLine ((int) Math.round(centX), 
		    (int) Math.round(centY), 
		    (int) Math.round(centX+len*Math.sin(ang)),
		    (int) Math.round(centY-len*Math.cos(ang)));
	g.drawLine ((int) Math.round(centX+len*Math.sin(ang)),
		    (int) Math.round(centY-len*Math.cos(ang)),
		    (int) Math.round(centX+len*Math.sin(ang)+
				     arrLen*Math.sin(Math.PI+ang-arrAng)),
		    (int) Math.round(centY-len*Math.cos(ang)-
				     arrLen*Math.cos(Math.PI+ang-arrAng)));
	g.drawLine ((int) Math.round(centX+len*Math.sin(ang)),
		    (int) Math.round(centY-len*Math.cos(ang)),
		    (int) Math.round(centX+len*Math.sin(ang)+
				     arrLen*Math.sin(Math.PI+ang+arrAng)),
		    (int) Math.round(centY-len*Math.cos(ang)-
				     arrLen*Math.cos(Math.PI+ang+arrAng)));
    }
  
    public void paint (Graphics g) {
	paintWrapper (g);
    }

    public void paintWrapper (Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	int n;
	String ax;

	// Draw the background window
	g2.setPaint (backCol);
	g2.fillRect (0,0,panXSize-1,panYSize-1);

	// Draw the Axes and Labels
	drawArrow (g2, axesCol, 
		   axSpace/2+bord, panYSize/2+bord,
		   axSpace/2, Math.PI/2, 5.0, Math.PI/8); 
	drawArrow (g2, axesCol, 
		   axSpace/2+bord, panYSize/2+bord,
		   axSpace/2,0.0, 5.0, Math.PI/8); 
	drawArrow (g2, axesCol, 
		   axSpace/2+bord, panYSize/2+bord,
		   axSpace/2, -0.8*Math.PI, 5.0, Math.PI/8); 
        Font font = new Font("SansSerif",Font.PLAIN,fontSize);
        g2.setFont (font);
        FontRenderContext frc = g2.getFontRenderContext();
	if (singlet) {
	    g2.setPaint (statCol);
	    ax = "ENTANGLED";
	    g2.drawString (ax, Math.round
			   (axSpace+3*bord+2*spinLength-
			    font.getStringBounds(ax,frc).getWidth()/2),
			   Math.round
			   (panYSize-bord-fontSpace/2+
			    font.getStringBounds(ax,frc).getHeight()/2));
	}
		 
	for (n=0; n<numberOfSpins; n++) {

	    // Draw the Spin Circles
	    g2.setPaint (circCol);
	    g2.drawOval (Math.round(centerX[n]-spinLength-1), 
			 Math.round(centerY[n]-spinLength-1),
			 Math.round(2*spinLength+2),
			 Math.round(2*spinLength+2));

	    // Draw the Labels
	    g2.setPaint (fontCol);
	    ax = ((numberOfSpins == 1)
			 ? "Single Spin" : "Spin "+(n+1));
	    g2.drawString (ax, Math.round
			   (centerX[n]-font.getStringBounds
			    (ax,frc).getWidth()/2),
			   Math.round
			   (panYSize-bord-fontSpace/2+
			    font.getStringBounds(ax,frc).getHeight()/2));
	
	    // Draw the Old Spin Arrows
	    if (showOldSpin)
		drawArrow (g2, oldSpinCol,centerX[n],centerY[n],
			   oldSpinLength,oldSpinAngle[n],
			   spinArrowLength,spinArrowAngle);
	
	    // Draw the Current Spin Arrows
	    if (!singlet) 
		drawArrow (g2, hiSpinAngle[n] ? spinHiCol : spinCol,
			   centerX[n], centerY[n],
			   spinLength, spinAngle[n], 
			   spinArrowLength, spinArrowAngle);

	    // Draw the Measurement Lines
	    g2.setPaint (measDirCol);
	    if (hiMeasAngle[n])
		g2.setPaint (measDirHiCol);
	    g2.drawLine ((int) Math.round(centerX[n]+
					  measDirLength*Math.sin(measAngle[n])-
					  measDirSep*Math.cos(measAngle[n])),
			 (int) Math.round(centerY[n]-
					  measDirLength*Math.cos(measAngle[n])-
					  measDirSep*Math.sin(measAngle[n])),
			 (int) Math.round(centerX[n]-
					  measDirLength*Math.sin(measAngle[n])-
					  measDirSep*Math.cos(measAngle[n])),
			 (int) Math.round(centerY[n]+
					  measDirLength*Math.cos(measAngle[n])-
					  measDirSep*Math.sin(measAngle[n])));
	    g2.drawLine ((int) Math.round(centerX[n]+
					  measDirLength*Math.sin(measAngle[n])+
					  measDirSep*Math.cos(measAngle[n])),
			 (int) Math.round(centerY[n]-
					  measDirLength*Math.cos(measAngle[n])+
					  measDirSep*Math.sin(measAngle[n])),
			 (int) Math.round(centerX[n]-
					  measDirLength*Math.sin(measAngle[n])+
					  measDirSep*Math.cos(measAngle[n])),
			 (int) Math.round(centerY[n]+
					  measDirLength*Math.cos(measAngle[n])+
					  measDirSep*Math.sin(measAngle[n])));
	}
    }
    
}

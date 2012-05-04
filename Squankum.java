/*****************************************************************************

    Squankum - Quantum Computing Simulation
    Version 0.4

    Copyright 2004, 2007, 2012 Jeffrey L. Wasserman
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*****************************************************************************/

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.lang.Math.*;
import java.util.Random;

public class Squankum extends JApplet {

    public qubitFrame fr;
	
    public void init() {
	setName ("Qubit-JApplet");
	fr = new qubitFrame();
	//fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void start() {
	fr.qubitInit();
    }

    public void stop() {
    }

    public void destroy() {
    }
}

// -------------------------------------------------------------------------

class qubitFrame extends JFrame {

    public QubitObject [] q;
    public Complex [][] operator;
    public int qFunc;
    public qubitPanel panDraw;
    public boolean ready;
    JPanel panCont;
    JComboBox funcCombo, operCombo;
    Container cont, boxCont, infoCont, selCont;
    Container sliderCont, funcCont;
    Container sphereCont, viewCont, topCont;
    JLabel rLab, thetaLab,phiLab;
    JLabel alphaLab,betaLab,gammaLab;
    JScrollBar thetaScroll, phiScroll;
    JScrollBar alphaScroll, betaScroll, gammaScroll;

    public qubitFrame () {


	super("Quantum Computation Applet");
	panDraw = new qubitPanel();
	panCont = new JPanel();
	operator = panDraw.pauliX;
	ready=false;

	String [] qubitFunctions = {"Choose a Function",
					"Single Qubit Representation",
				    "Single Qubit Operations",
				    "Two Qubit Representation (Not Finished)",
				    "Two Qubit Operations (Not Finished)",
				    "Bell (Entangled) States (Not Implemented)",
				    "Quantum Teleportation (Not Implemented)"};
	funcCombo = new JComboBox(qubitFunctions);
	funcCombo.setSelectedIndex(0);

	alphaLab = new JLabel(" ");
	betaLab = new JLabel(" "); 
	gammaLab = new JLabel(" ");

	float pi=(float)Math.PI;
        alphaScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      Math.round(panDraw.alpha/pi*180),
				      1, 0, 361);
        betaScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      Math.round(panDraw.beta/pi*180),
				      1, 0, 361);
        gammaScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      Math.round(panDraw.gamma/pi*180),
				      1, 0, 361);

	selCont = Box.createVerticalBox();
	boxCont = Box.createVerticalBox();
	funcCont = Box.createVerticalBox();
	sliderCont = Box.createHorizontalBox();
	infoCont = Box.createVerticalBox();
	sphereCont = Box.createVerticalBox();
	viewCont = Box.createVerticalBox();
	topCont = Box.createHorizontalBox();

	/*
	sphereCont.add(new JLabel("Input Qubit"));
	sphereCont.add(thetaLab);
	sphereCont.add(thetaScroll);
	sphereCont.add(phiLab);
	sphereCont.add(phiScroll);
	*/

	viewCont.add(new JLabel("Viewing Angle"));
	viewCont.add(alphaLab);
	viewCont.add(alphaScroll);
	viewCont.add(betaLab);
	viewCont.add(betaScroll);
	viewCont.add(gammaLab);
	viewCont.add(gammaScroll);

	infoCont.add(new JLabel("Squankum"));
	infoCont.add(new JLabel("Quantum Computation Simulation"));
	infoCont.add(new JLabel("www.pha.jhu.edu/~jeffwass/squankum"));


	selCont.add(Box.createVerticalGlue());
	selCont.add(viewCont);
	selCont.add(Box.createVerticalStrut(10));
	selCont.add(new JLabel("Quantum Computation Function"));
	selCont.add(funcCombo);
	selCont.add(Box.createVerticalStrut(10));
	selCont.add(funcCont);
	selCont.add(Box.createVerticalStrut(10));
	selCont.add(Box.createVerticalGlue());
	selCont.add(infoCont);
	selCont.add(Box.createVerticalGlue());

	boxCont.add(sliderCont);

	topCont.add(panDraw);
	topCont.add(Box.createHorizontalStrut(20));
	topCont.add(selCont);
	topCont.add(Box.createHorizontalStrut(20));

	cont = getContentPane();
	cont.setLayout(new BorderLayout());
	cont.add(topCont, BorderLayout.NORTH);
	cont.add(boxCont, BorderLayout.SOUTH);
	//cont.setLayout(new FlowLayout());
	//cont.add(topCont);
	//cont.add(boxCont);

	pack();
	setVisible(true);

	panDraw.parentFrame = this;


        alphaScroll.addAdjustmentListener (new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    int alphaDeg=alphaScroll.getValue();
                    panDraw.alpha = (float)Math.PI*alphaDeg/180;
                    alphaLab.setText("Alpha = "+alphaDeg);
                    panDraw.repaint();
                }
            });
        betaScroll.addAdjustmentListener (new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    int betaDeg=betaScroll.getValue();
                    panDraw.beta = (float)Math.PI*betaDeg/180;
                    betaLab.setText("Beta = "+betaDeg);
                    panDraw.repaint();
                }
            });             
        gammaScroll.addAdjustmentListener (new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    int gammaDeg=gammaScroll.getValue();
                    panDraw.gamma = (float)Math.PI*gammaDeg/180;
                    gammaLab.setText("Gamma = "+gammaDeg);
                    panDraw.repaint();
                }
            });  

	funcCombo.addActionListener (new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    sliderCont.removeAll();
		    funcCont.removeAll();
		    JComboBox cb = (JComboBox)e.getSource();
		    qubitFrame p = (qubitFrame)cb.getTopLevelAncestor();
		    qFunc = cb.getSelectedIndex();
		    System.out.println("about to do funcCombo case");
		    switch (qFunc) {
		    case 0:
			break;
			case 1:
			System.out.println("case 1 selected");
			p.panDraw.numberOfQubitObjects=1;
			q = new QubitObject[1];
			SingleQubit q0 = new SingleQubit();
			q[0] = q0;
			q[0].setCenter (panDraw.centerX1,
					panDraw.centerY);
			q[0].setLabel ("Single Qubit");
			q[0].buildWidget();
			sliderCont.add(q0.qCont);
			q0.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			sliderCont.add(Box.createHorizontalStrut(5));		     
			q0.parentFrame.pack();
			panDraw.qubitUpdate();
			break;
		    case 2:
			System.out.println("Captured a TWO");
			panDraw.numberOfQubitObjects=2;
			q = new QubitObject[2];
			SingleQubit q10 = new SingleQubit();
			SingleQubit q11 = new SingleQubit();
			q[0] = q10;
			q[1] = q11;
			q[0].setLabel("Input Qubit");
			q[1].setLabel("Resulting Qubit");
			q11.setNonControl(true);
			q[0].buildWidget();
			q[1].buildWidget();
			q[0].setCenter (panDraw.centerX2A,
					panDraw.centerY);
			q[1].setCenter (panDraw.centerX2B,
					panDraw.centerY);
			sliderCont.add(q10.qCont);
			sliderCont.add(Box.createHorizontalStrut(5));
			sliderCont.add(q11.qCont);
			q10.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			q11.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			String [] singleQubitOperations = {
			    "PauliX",
			    "PauliY",
			    "PauliZ",
			    "Hadamard",
			    "Phase (S)",
			    "Pi/8 (T)"};
			System.out.println("Doing the opercombo thing");
			operCombo = new JComboBox(singleQubitOperations);
			operCombo.setSelectedIndex(0);
			funcCont.add(new JLabel("Single Qubit Operator"));
			funcCont.add(operCombo);
			pack();
			operCombo.addActionListener (new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    JComboBox cb2 = (JComboBox)e.getSource();
				    int si = cb2.getSelectedIndex();
				    switch (si) {
				    case 0 : 
					operator=panDraw.pauliX;
					break;
				    case 1 :
					operator=panDraw.pauliY;
					break;
				    case 2 :
					operator=panDraw.pauliZ;
					break;
				    case 3 : 
					operator=panDraw.hadamard;
					break;
				    case 4 :
					operator=panDraw.phaseS;
					break;
				    case 5 :
					operator=panDraw.phaseT;
					break;
				    default :
					operator=panDraw.pauliZ;
					break;
				    }
				    panDraw.qubitUpdate();
				    panDraw.repaint();
				}
				});
			operCombo.setSelectedIndex(0);
			System.out.println("about to update qubit");
			panDraw.qubitUpdate();
			break;
		    case 3:
			p.panDraw.numberOfQubitObjects=1;
			q = new QubitObject[1];
			DoubleQubit q2 = new DoubleQubit();
			q[0] = q2;
			q[0].setCenter (panDraw.centerX1,
					panDraw.centerY);
			q[0].setLabel ("Double Qubit (Not Implemented)");
			q[0].buildWidget();
			sliderCont.add(q2.qCont);
			q2.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			sliderCont.add(Box.createHorizontalStrut(5));
			q2.parentFrame.pack();
			panDraw.qubitUpdate();
			break;
		    case 4:
			System.out.println("Captured a FOUR");
			panDraw.numberOfQubitObjects=2;
			q = new QubitObject[2];
			DoubleQubit q20 = new DoubleQubit();
			DoubleQubit q21 = new DoubleQubit();
			q[0] = q20;
			q[1] = q21;
			q[0].setLabel("Input Double Qubit");
			q[1].setLabel("Resulting Double Qubit");
			q21.setNonControl(true);
			q[0].buildWidget();
			q[1].buildWidget();
			q[0].setCenter (panDraw.centerX2A,
					panDraw.centerY);
			q[1].setCenter (panDraw.centerX2B,
					panDraw.centerY);
			sliderCont.add(q20.qCont);
			sliderCont.add(Box.createHorizontalStrut(5));
			sliderCont.add(q21.qCont);
			q20.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			q21.parentFrame = (qubitFrame)cb.getTopLevelAncestor();
			String [] doubleQubitOperations = {
			    "Controlled Not",
			    "Controlled Z"};
			operCombo = new JComboBox(doubleQubitOperations);
			operCombo.setSelectedIndex(0);
			funcCont.add(new JLabel("Double Qubit Operator"));
			funcCont.add(operCombo);
			pack();
			operCombo.addActionListener (new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    JComboBox cb2 = (JComboBox)e.getSource();
				    int si = cb2.getSelectedIndex();
				    switch (si) {
				    case 0 : 
					operator=panDraw.controlledNot;
					break;
				    case 1 :
					operator=panDraw.controlledZ;
					break;
				    default :
					operator=panDraw.controlledNot;
					break;
				    }
				    panDraw.qubitUpdate();
				    panDraw.repaint();
				}
				});
			operCombo.setSelectedIndex(0);
			panDraw.qubitUpdate();
			break;
		    case 5:
			break;
		    default:
			break;
			}
		    p.getContentPane().validate();
		}
	    });
	funcCombo.setSelectedIndex(0);
	ready=true;
    }

    public void qubitInit () {
	System.out.println("qubitInit");
	panDraw.numberOfQubitObjects=0;
	//	panDraw.spinInit();
	panDraw.qubitUpdate();
	//panDraw.repaint();
    }
    
    public String formStr (float f) {
	int i=Math.round(f*1000);
	return new String(""+i/1000.0);
    }
}

//------------------------------------------------------------------------

class Complex {

    private float re;
    private float im;
    public float pi=(float)Math.PI;

    public Complex() {
	re=0;
	im=0;
    }

    public Complex (float R, float I) {
	re=R;
	im=I;
    }

    public float getRe () {
	return re;
    }

    public float getIm () {
	return im;
    }

    public void set (float R, float I) {
	re=R;
	im=I;
    }

    public Complex clone (Complex v) {
	return new Complex (v.re, v.im);
    }

    public float norm () {
	return (float) Math.sqrt(re*re+im*im);
    }

    public float phase () {
	float d= (re>0f) ? 0.000001f : -0.000001f;
	float t = (float) Math.atan(im/(re+d));
	if (re < 0) t += pi;
	return ((t<0) ? t+2*pi : t);
    }

    public Complex sum (Complex v1, Complex v2) {
	return new Complex (v1.re+v2.re, v1.im+v2.im);
    }

    public Complex scalar (float f) {
	return new Complex (f*re,f*im);
    }
 
    public Complex product (Complex v1, Complex v2) {
	return new Complex (v1.re*v2.re-v1.im*v2.im,
			    v1.re*v2.im+v1.im*v2.re);
    }

    public String print () {
	String a;
	a="("+re+" , "+im+")";
	return a;
    }
}

//----------------------------------------------------------------------

interface QubitObject {
    void buildWidget();
    void updateWidget();
    void paintWidget();
    Complex[] operate (Complex[][] operator);
    String print();
    void setCenter (float x, float y);
    void setLabel (String l);
}

//-----------------------------------------------------------------------

class SingleQubit implements QubitObject {

    public Complex c0 = new Complex(0f,0f);
    public Complex c1 = new Complex(1f,0f);
    public Complex ci = new Complex(0f,1f);
    private Complex[] spinor = {new Complex(1f,0f), new Complex(0f,0f)};
    public qubitFrame parentFrame;
    public Container qCont;
    public JScrollBar thetaScroll, phiScroll, phaseScroll;
    public JLabel title, thetaLab, phiLab, phaseLab;
    public qubitTableModel tabModel;
    public JTable table;
    private float pi=(float)Math.PI;
    private boolean nonControl;
    private float centerX, centerY;
    private String label;

    public SingleQubit () {
    }

    public SingleQubit (Complex a, Complex b) {
	Complex spinor [] = {a,b};
    }

    public void setCenter (float x, float y) {
	centerX=x;
	centerY=y;
    }

    public void setNonControl (boolean b) {
	nonControl=b;
    }

    public void setLabel (String l) {
	label = l;
    }

    public Complex getA() {
	return spinor[0];
    }

    public Complex getB() {
	return spinor[1];
    }

    public void setA (Complex a) {
	spinor[0]=a;
    }

    public void setB (Complex b) {
	spinor[1]=b;
    }

    public void setAB (Complex a[]) {
	spinor[0]=a[0];
	spinor[1]=a[1];
    }

    public float getTheta() {
	return 2f*(float)Math.atan(spinor[1].norm()/(0.00001f+spinor[0].norm()));
    }

    public float getPhi() {
	float ph=spinor[1].phase()-spinor[0].phase();
	return ((ph<0) ? ph+=2*3.14159 : ph);
    }

    public float getPhase() {
	return spinor[0].phase();
    }

    public void setThetaPhiPhase (float theta, float phi, float phase) {
	Complex phaseFactor = new Complex ((float)Math.cos(phase),
					   (float)Math.sin(phase));
	spinor[0].set ((float)(Math.cos(theta/2f)),0);
	spinor[1].set ((float)(Math.sin(theta/2f)*Math.cos(phi)),
			 (float)(Math.sin(theta/2f)*Math.sin(phi)));
	spinor[0]=spinor[0].product(spinor[0],phaseFactor);
	spinor[1]=spinor[1].product(spinor[1],phaseFactor);
    }

    public void buildWidget(){
	qCont = Box.createVerticalBox();
	//qCont.setAlignmentX(Component.CENTER_ALIGNMENT);
	thetaScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      0, 1, 0, 181);
        phiScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				    0, 1, 0, 361);
        phaseScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      0, 1, 0, 361);
	tabModel = new qubitTableModel();
	table = new JTable (tabModel);
	title = new JLabel(label);
	thetaLab = new JLabel("Theta");
	phiLab = new JLabel("Phi");
	phaseLab = new JLabel("Phase");
	qCont.add (title);
	qCont.add(Box.createVerticalStrut(10));
	qCont.add (thetaLab);
	qCont.add (thetaScroll);
	qCont.add (phiLab);
	qCont.add (phiScroll);
	qCont.add (phaseLab);
	qCont.add (phaseScroll);
	qCont.add (table);
	if (nonControl) {
	    thetaScroll.setEnabled(false);
	    phiScroll.setEnabled(false);
	    phaseScroll.setEnabled(false);
	}
	else {
	    thetaScroll.addAdjustmentListener (new AdjustmentListener() {
		    public void adjustmentValueChanged(AdjustmentEvent e) {
			System.out.println("inside thetascroll");
			float th=(float)(thetaScroll.getValue()/180f*Math.PI);
			float ph=(float)(phiScroll.getValue()/180f*Math.PI);
			float phase=(float)(phaseScroll.getValue()/180f*Math.PI);
			setThetaPhiPhase(th,ph,phase);
			parentFrame.panDraw.qubitUpdate();
			//parentFrame.repaint();
			
		    }
		});
	    phiScroll.addAdjustmentListener (new AdjustmentListener() {
		    public void adjustmentValueChanged(AdjustmentEvent e) {
			float ph=(float)(phiScroll.getValue()/180f*Math.PI);
			float phase=(float)(phaseScroll.getValue()/180f*Math.PI);
			setThetaPhiPhase(getTheta(),ph,phase);
			parentFrame.panDraw.qubitUpdate();
			//parentFrame.repaint();
		    }
		});
	    phaseScroll.addAdjustmentListener (new AdjustmentListener() {
		    public void adjustmentValueChanged(AdjustmentEvent e) {
			float phase=(float)(phaseScroll.getValue()/180f*Math.PI);
			setThetaPhiPhase(getTheta(),getPhi(),phase);
			parentFrame.panDraw.qubitUpdate();
			//parentFrame.repaint();
		    }
		});
	}
    }

    public void updateWidget() {
	table.setValueAt (new Float(Math.round
				    (1000f*getA().getRe())/1000f),1,1);
	table.setValueAt (new Float(Math.round
				    (1000f*getA().getIm())/1000f),1,2);
	table.setValueAt (new Float(Math.round
				    (1000f*getB().getRe())/1000f),2,1);
	table.setValueAt (new Float(Math.round
				    (1000f*getB().getIm())/1000f),2,2);
	table.setValueAt (new Float(Math.round
				    (1000f*Math.pow(getA().norm(),2))/10f),1,3);
	table.setValueAt (new Float(Math.round
				    (1000f*Math.pow(getB().norm(),2))/10f),2,3);
	if (nonControl) {
	    thetaScroll.setValue(Math.round(getTheta()*180/pi));
	    phiScroll.setValue(Math.round(getPhi()*180/pi));
	    phaseScroll.setValue(Math.round(getPhase()*180/pi));
	}
	thetaLab.setText("Theta : "+Math.round(getTheta()*180/pi));
	phiLab.setText("Phi : "+Math.round(getPhi()*180/pi));
	phaseLab.setText("Overall Phase "+Math.round(getPhase()*180/pi));
    }

    public void paintWidget() {

	// paint the Qubit
	System.out.println("inside paintWidget");
	System.out.println("theta is "+getTheta()+" -- Phi is "+getPhi());
	float sl=parentFrame.panDraw.spinLength;
	float h=(float)parentFrame.panDraw.font.getStringBounds
	    (label,parentFrame.panDraw.frc).getHeight();
	parentFrame.panDraw.paintQubit(centerX,
				       centerY,
				       sl,
				       getTheta(),
				       getPhi(),
				       label,
				       true);
	parentFrame.panDraw.paintLabel(centerX,
				       centerY+sl+3.75f*h,
				       label);
    }

    public Complex[] operate (Complex [][] operator) {
	Complex a=c0.sum(c0.product(operator[0][0],spinor[0]),
			 c0.product(operator[0][1],spinor[1]));
	Complex b=c0.sum(c0.product(operator[1][0],spinor[0]),
			 c0.product(operator[1][1],spinor[1]));
	return new Complex[] {a,b};
    }
     
    public String print() {
	String a;
	a="["+spinor[0].print()+" , "+spinor[1].print()+"]";
	return a;
    }

    class qubitTableModel extends AbstractTableModel {
	private String[] headings = new String[] {
	    "State","Re","Im","Pct"};
	private Object[][] data = new Object[][] {
	    {"State","Real", "Imag.","Pct. Prob."},
	    {"|0>"," "," "," "},
	    {"|1>"," "," "," "}
	};

	public int getRowCount() {return data.length;}
	public int getColumnCount() {return data[0].length;}
	public Object getValueAt(int row, int column) {
	    return data[row][column];}
	public String getColumnName (int column) {
	    return headings[column];
	}
	public Class getColumnClass(int column) {
	    return data[0][column].getClass();
	}
	public void setValueAt (Object value, int row, int column) {
	    data[row][column] = value;
	    fireTableDataChanged();
	}
    }  
}

//-------------------------------------------------------------------

class DoubleQubit implements QubitObject {

    public Complex c0 = new Complex(0f,0f);
    public Complex c1 = new Complex(1f,0f);
    public Complex ci = new Complex(0f,1f);
    private Complex[] spinor = {new Complex(1f,0f), new Complex(0f,0f),
				new Complex (0f,0f), new Complex(0f,0f)};
    public qubitFrame parentFrame;
    public Container qCont;
    public JScrollBar [] thetaScroll, phiScroll; 
    public JScrollBar phaseScroll;
    public JLabel [] thetaLab, phiLab;
    public JLabel title, phaseLab;
    public qubitTableModel tabModel;
    public JTable table;
    private float pi=(float)Math.PI;
    private boolean nonControl;
    private float centerX, centerY;
    private String label;

    public DoubleQubit () {
    }

    public DoubleQubit (Complex [] qIn) {
	for (int i=0; i<4; i++) 
	    spinor [i] = qIn[i];
    }

    public void setCenter (float x, float y) {
	centerX=x;
	centerY=y;
    }

    public void setNonControl (boolean b) {
	nonControl=b;
    }

    public void setLabel (String l) {
	label = l;
    }

    public Complex getN(int n) {
	return spinor[n];
    }

    public void setN (int n,
		      Complex a) {
	spinor[n]=a;
    }

    public void setSpinors (Complex qIn[]) {
	for (int i=0; i<4; i++) spinor [i]=qIn[i];
    }

    public void setCoupled (SingleQubit q1, SingleQubit q2) {
	Complex d=new Complex();
	spinor[0]=d.product(q1.getA(),q2.getA());
	spinor[1]=d.product(q1.getA(),q2.getB());
	spinor[2]=d.product(q1.getB(),q2.getA());
	spinor[3]=d.product(q2.getB(),q2.getB());
    }

    public float normN (Complex qIn[],
			int n) {
	float sum=0f;
	for (int i=0; i<n; i++) 
	    sum+=(float)(Math.pow(qIn[i].norm(),2));
	return (float) Math.sqrt(sum);
    }

    public float getTheta(int n) {
	switch (n) {
	case 0:
	    return 2f*(float)Math.asin
		(normN(new Complex[] {spinor[2],spinor[3]},2));
	case 1:
	    return 2f*(float)Math.asin
		(normN(new Complex[] {spinor[1],spinor[3]},2));
	case 2:
	    return (float) 0;
	}
	return 0;
    }

    public float getPhi(int n) {
	return 0;
    }

    public float getPhase() {
	return 0;
    }

    public void setThetaPhiPhase (float []theta, 
				  float []phi, 
				  float phase) {

	Complex phaseFactor = new Complex ((float)Math.cos(phase),      
					   (float)Math.sin(phase));
	float[] dummy = new float[4];
	
	float s0=(float)(Math.pow(Math.sin(theta[0]/2f),2));
	float s1=(float)(Math.pow(Math.sin(theta[1]/2f),2));
	float c0=(float)(Math.pow(Math.cos(theta[0]/2f),2));
	float c1=(float)(Math.pow(Math.cos(theta[1]/2f),2));

	//dummy[0]=(float)Math.sqrt(c0*c1);
	//dummy[1]=(float)Math.sqrt(c0*s1);
	//dummy[2]=(float)Math.sqrt(s0*c1);
	//dummy[3]=(float)Math.sqrt(s0*s1);
	dummy[0]=(float)(c0*c1);
	dummy[1]=(float)(c0*s1);
	dummy[2]=(float)(s0*c1);
	dummy[3]=(float)(s0*s1);
	
	// set the magnitude of the spinors
	spinor[0].set((float)Math.sqrt(dummy[0]),0f);
	spinor[1].set((float)Math.sqrt(dummy[1]),0f);
	spinor[2].set((float)Math.sqrt(dummy[2]),0f);
	spinor[3].set((float)Math.sqrt(dummy[3]),0f);
	
    }

    public void buildWidget(){
	int i,j;
	
	System.out.println("hi");
	qCont = Box.createVerticalBox();
	System.out.println("yo");
	thetaLab = new JLabel[3];
	phiLab = new JLabel[3];
	thetaScroll = new JScrollBar[3];
	phiScroll = new JScrollBar[3];
	for (i=0;i<3;i++) {
	    thetaScroll[i]=new JScrollBar (JScrollBar.HORIZONTAL,
					   0, 1, 0, 181);
	    phiScroll[i]=new JScrollBar (JScrollBar.HORIZONTAL,
					 0, 1, 0, 361);
	}
	phaseScroll = new JScrollBar (JScrollBar.HORIZONTAL,
				      0, 1, 0, 361);	
	tabModel = new qubitTableModel();
	table = new JTable (tabModel);
	title = new JLabel(label);
	thetaLab[0] = new JLabel("Theta Qubit 1");
	phiLab[0] = new JLabel("Phi Qubit 1");
	thetaLab[1] = new JLabel("Theta Qubit 2");
	phiLab[1] = new JLabel("Phi Qubit 2");
	thetaLab[2] = new JLabel("Theta Coupling");
	phiLab[2] = new JLabel("Phi Coupling");
	phaseLab = new JLabel("Phase");
	qCont.add (title);
	qCont.add(Box.createVerticalStrut(10));
	for (i=0;i<3;i++) {
	    qCont.add (thetaLab[i]);
	    qCont.add (thetaScroll[i]);
	    qCont.add (phiLab[i]);
	    qCont.add (phiScroll[i]);
	}
	qCont.add (phaseLab);
	qCont.add (phaseScroll);
	qCont.add (table);
	if (nonControl) {
	    for (i=0;i<3;i++){
		thetaScroll[i].setEnabled(false);
		phiScroll[i].setEnabled(false);
	    }
	    phaseScroll.setEnabled(false);
	}
	else {
	    for (i=0;i<3;i++) {
		thetaScroll[i].addAdjustmentListener (new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
			    //System.out.println("inside thetascroll"+i);
			    int j;
			    float [] th = new float[3];
			    float [] ph = new float[3];
			    for (j=0;j<3;j++) {
				th[j]=(float)(thetaScroll[j].getValue()
						 /180f*Math.PI);
				ph[j]=(float)(phiScroll[j].getValue()
						 /180f*Math.PI);
			    }
			    float phase=(float)(phaseScroll.getValue()/180f*Math.PI);
			    setThetaPhiPhase(th,ph,phase);
			    parentFrame.panDraw.qubitUpdate();
			    //parentFrame.repaint();
			    
			}
		    });
		phiScroll[i].addAdjustmentListener (new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
			    int j;
			    float [] th = new float[3];
			    float [] ph = new float[3];
			    for (j=0;j<3;j++) {
				th[j]=getTheta(j);
				ph[j]=(float)(phiScroll[j].getValue()
						 /180f*Math.PI);
			    }
			    float phase=(float)(phaseScroll.getValue()
						/180f*Math.PI);
			    setThetaPhiPhase(th,ph,phase);
			    parentFrame.panDraw.qubitUpdate();
			    //parentFrame.repaint();
			}
		    });
	    }
	    phaseScroll.addAdjustmentListener (new AdjustmentListener() {
		    public void adjustmentValueChanged(AdjustmentEvent e) {
			int j;
			float [] th = new float[3];
			float [] ph = new float[3];
			for (j=0;j<3;j++) {
			    th[j]=getTheta(j);
			    ph[j]=getPhi(j);
			}
			float phase=(float)(phaseScroll.getValue()/180f*Math.PI);
			setThetaPhiPhase(th,ph,phase);
			parentFrame.panDraw.qubitUpdate();
			//parentFrame.repaint();
		    }
		});
	}
    }

    public void updateWidget() {
	int i;
	for (i=0;i<4;i++) {
	    table.setValueAt (new Float(Math.round
					(1000f*getN(i).getRe())/1000f),i+1,1);
	    table.setValueAt (new Float(Math.round
					(1000f*getN(i).getIm())/1000f),i+1,2);
	    table.setValueAt (new Float(Math.round
					(1000f*Math.pow(getN(i).norm(),2))/10f),i+1,3);
	}
	if (nonControl) {
	    for (i=0;i<3;i++) {
		thetaScroll[i].setValue(Math.round(getTheta(i)*180/pi));
		phiScroll[i].setValue(Math.round(getPhi(i)*180/pi));
	    }
	    phaseScroll.setValue(Math.round(getPhase()*180/pi));
	}
	thetaScroll[2].setValue(Math.round(getTheta(2)*180/pi));
	String qlabs[] = {"Qubit A","Qubit B","Entanglement"};
	for (i=0;i<3;i++) {
	    thetaLab[i].setText(qlabs[i]+ " Theta : "+Math.round(getTheta(i)*180/pi));
	    phiLab[i].setText(qlabs[i]+" Phi : "+Math.round(getPhi(i)*180/pi));
	}
	phaseLab.setText("Overall Phase : "+Math.round(getPhase()*180/pi));
    }

    public void paintWidget() {

	System.out.println("theta0 is "+getTheta(0)+" -- Phi is "+getPhi(0));
	System.out.println("theta1 is "+getTheta(1)+" -- Phi is "+getPhi(1));
	System.out.println("theta2 is "+getTheta(2)+" -- Phi is "+getPhi(2));
	float sl=parentFrame.panDraw.spinLength;
	float h=(float)parentFrame.panDraw.font.getStringBounds
	    (label,parentFrame.panDraw.frc).getHeight();
	parentFrame.panDraw.paintLabel(centerX,centerY+sl+3.75f*h,label);
	parentFrame.panDraw.paintQubit(centerX-0.6f*sl,
				       centerY-0.4f*sl,
				       0.5f*sl,
				       getTheta(0),
				       getPhi(0),
				       label,
				       false);
	parentFrame.panDraw.paintLabel(centerX-0.6f*sl,
				       centerY-0.4f*sl-3f*h,
				       "Qubit A");
	parentFrame.panDraw.paintQubit(centerX+0.6f*sl,
				       centerY-0.4f*sl,
				       0.5f*sl,
				       getTheta(1),
				       getPhi(1),
				       label,
				       false);
	parentFrame.panDraw.paintLabel(centerX+0.6f*sl,
				       centerY-0.4f*sl-3f*h,
				       "Qubit B");
	parentFrame.panDraw.paintQubit(centerX,
				       centerY+0.65f*sl,
				       0.5f*sl,
				       getTheta(2),
				       getPhi(2),
				       label,
				       false);
	parentFrame.panDraw.paintLabel(centerX,
				       centerY+1.15f*sl+1.5f*h,
				       "Entanglement");
    }

    
    public Complex[] operate (Complex [][] operator) {
	System.out.println("aaa");
	Complex [] retVal;
	System.out.println("bbb");
	retVal = new Complex [4];
	System.out.println("ccc");
	for (int i=0; i<4; i++) {
	    System.out.println("Retval prob - i is "+i);
	    Complex dumd = new Complex();
	    dumd.set(0f,0f);
	    retVal [i] = dumd;
	    System.out.println("eee");
	    for (int j=0; j<4; j++) {
		System.out.println("operate = i,j : "+i+","+j);
		retVal[i]=c0.sum(retVal[i],
				 c0.product(operator[i][j],spinor[j]));
	    }
	}
	return retVal;
    }
     
    public String print() {
	String a;
	a="[";
	for (int i=0;i<2;i++) 
	    a+=spinor[i].print();
	a+=spinor[3].print()+"]";
	return a;
    }

    class qubitTableModel extends AbstractTableModel {
	private String[] headings = new String[] {
	    "State","Re","Im","Pct"};
	private Object[][] data = new Object[][] {
	    {"State","Real", "Imag.","Pct. Prob."},
	    {"|00>"," "," "," "},
	    {"|01>"," "," "," "},
	    {"|10>"," "," "," "},
	    {"|11>"," "," "," "}
	};

	public int getRowCount() {return data.length;}
	public int getColumnCount() {return data[0].length;}
	public Object getValueAt(int row, int column) {
	    return data[row][column];}
	public String getColumnName (int column) {
	    return headings[column];
	}
	public Class getColumnClass(int column) {
	    return data[0][column].getClass();
	}
	public void setValueAt (Object value, int row, int column) {
	    data[row][column] = value;
	    fireTableDataChanged();
	}
    }  
}

//-------------------------------------------------------------------

class qubitPanel extends JPanel {

    public qubitFrame parentFrame;
    private Graphics2D g2;

    public float bord=10.0f;
    public float axSpace=50.0f;
    public float fontSpace=20.0f;
    public int fontSize=12;
    public int panXSize;
    public int panYSize;

    public float maxLength=100.0f;
    public float spinLength=maxLength*0.8f;

    public float pi=(float)Math.PI;
    public float alpha=pi/8;
    public float beta=1.95f*pi;
    public float gamma=0;

    public float measDirLength=50.0f;
    public float oldSpinLength=75.0f;
    public float axLength=axSpace/2f;
    public float measDirSep=2.0f;
    float spinArrowLength=15.0f;
    float spinArrowAngle=(float)(Math.PI/8.0);
    float angleResolution=(float)(Math.PI*3.0f/180.0f);
    float hiLiteRange=8.0f;

    public boolean sphereLock=false;
    public boolean cartLock=false;

    float axX=bord+axLength;
    float axY;
    int axOv=2;
    public int angRef=1;
    public int numberOfQubitObjects;
    public float centerX1,centerX2A,centerX2B,centerY;
    float[] dotline = {2f,2f};
    int eigenRad=3;

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
    Color axesCol = new Color (0,0xaa,0xaa);
    Color equatorCol = new Color (0,0x77,0x77);
    Color guideCol = new Color (0x77,0x77,0x77);
    Color fontCol = new Color (0xFF,0xFF,0xFF);
    Color statCol = new Color (0xFF,0x33,0x33);
    Color zeroCol = new Color (0xFF,0x00,0x00);
    Color oneCol = new Color (0,0,0xFF);

    public Complex [][] operator;
    SingleQubit d = new SingleQubit ();
    DoubleQubit dd = new DoubleQubit ();
    float s2 = (float)Math.sqrt(2f)/2f;
    Complex [][] pauliX={{d.c0,d.c1},{d.c1,d.c0}};
    Complex [][] pauliY={{d.c0,d.ci.scalar(-1f)},{d.ci,d.c0}};
    Complex [][] pauliZ={{d.c1,d.c0},{d.c0,d.c1.scalar(-1f)}};
    Complex [][] hadamard={{d.c1.scalar(s2),d.c1.scalar(s2)},
			   {d.c1.scalar(s2),d.c1.scalar(-1f*s2)}};
    Complex [][] phaseS={{d.c1,d.c0},{d.c0,d.ci}};
    Complex [][] phaseT={{d.c1,d.c0},{d.c0,new Complex(s2,s2)}};
    Complex [][] controlledNot={{d.c1,d.c0,d.c0,d.c0},
				{d.c0,d.c1,d.c0,d.c0},
				{d.c0,d.c0,d.c0,d.c1},
				{d.c0,d.c0,d.c1,d.c0}};
    Complex [][] controlledZ={{d.c1,d.c0,d.c0,d.c0},
				{d.c0,d.c1,d.c0,d.c0},
				{d.c0,d.c0,d.c1,d.c0},
				{d.c0,d.c0,d.c0,d.c1.scalar(-1f)}};


    public Font font;
    public FontRenderContext frc;

    public qubitPanel () {

	parentFrame = (qubitFrame) getTopLevelAncestor();
	int n;
//	q = new QubitObject [numberOfQubitObjects];
//	for (n=0;n<numberOfSpins;n++) {
//    q[n]=new Qubit(new Complex(s2,0),
//	   new Complex(0.5f,0.5f));
//	}
	panXSize=Math.round(4.0f*bord+fontSpace+axSpace+4.0f*maxLength);
	panYSize=Math.round(3.0f*bord+fontSpace+2.0f*maxLength);
	setPreferredSize (new Dimension(panXSize,panYSize));
	axY=bord+panYSize/2;
	centerY=bord+maxLength;
	centerX1=axSpace + 2.5f*bord + 2*maxLength;
	centerX2A=axSpace + (2f*bord) + maxLength;
	centerX2B=axSpace + (3f*bord) + 3*maxLength;
    }

    /*
    public void spinInit () {
	int n;

	centerX[0] = axSpace + (2f*bord) + maxLength;
	centerX[1] = axSpace + (3f*bord) + 3*maxLength;
	centerY[0] = bord + maxLength;
	centerY[1] = bord + maxLength;
	//qubitUpdate();
	repaint();
    }
    */

    public void qubitUpdate () {
	System.out.println("inside qubitUpdate");
	qubitFrame p = (qubitFrame) getTopLevelAncestor();
	switch (p.qFunc) {
	case 0:
		break;
	case 1:
	    p.q[0].updateWidget();
	    break;
	case 2:
	    SingleQubit d = (SingleQubit) p.q[1];
	    d.setAB(p.q[0].operate(p.operator));
	    p.q[0].updateWidget();
	    p.q[1].updateWidget();
	    break;
	case 3:
	    p.q[0].updateWidget();
	    break;
	case 4:
	    System.out.println("a");
	    DoubleQubit dd = (DoubleQubit) p.q[1];
	    System.out.println("b");
	    dd.setSpinors(p.q[0].operate(p.operator));
	    System.out.println("c");
	    p.q[0].updateWidget();
	    p.q[1].updateWidget();
	    break;
	case 5:
	    break;
	}
	p.alphaLab.setText("Alpha "+Math.round(alpha*180/pi));
	p.betaLab.setText("Beta "+Math.round(beta*180/pi));
	p.gammaLab.setText("Gamma "+Math.round(gamma*180/pi));
	repaint();
    }

    public float thetaPhiToX (float th,
			      float ph) {

	double xx=(-Math.sin(gamma)*Math.cos(beta)*Math.cos(alpha)-
		   Math.cos(gamma)*Math.sin(alpha))*Math.sin(th)*Math.cos(ph)+
	    (-Math.sin(gamma)*Math.cos(beta)*Math.sin(alpha)+
	     Math.cos(gamma)*Math.cos(alpha))*Math.sin(th)*Math.sin(ph)+
	    Math.sin(gamma)*Math.sin(beta)*Math.cos(th);
	return (float)xx;
    }

    public float thetaPhiToY (float th,
			    float ph) {
	double yy = Math.sin(beta)*Math.cos(alpha)*Math.sin(th)*Math.cos(ph) +
	    Math.sin(beta)*Math.sin(alpha)*Math.sin(th)*Math.sin(ph)+
	    Math.cos(beta)*Math.cos(th);
	return (float)yy;
    }

    public void drawLatitude (Graphics2D g2,
			    float centX,
			    float centY,
			    float len,
			    float th) {

	int st=30;

	for (int i=0;i<st;i++) {
	    g2.drawLine ((int) Math.round(centX+len*thetaPhiToX(th,i*2f*pi/st)),
			 (int) Math.round(centY-len*thetaPhiToY(th,i*2f*pi/st)),
			 (int) Math.round(centX+len*thetaPhiToX(th,(i+1)*2f*pi/st)),
			 (int) Math.round(centY-len*thetaPhiToY(th,(i+1)*2f*pi/st)));
	}
    }

    public void paintLabel (float centX,
			    float centY,
			    String label) {

	g2.setPaint (fontCol);
	g2.drawString (label, Math.round
		       (centX-font.getStringBounds
			(label,frc).getWidth()/2),
		       Math.round(centY-font.getStringBounds(label,frc).getHeight()/2));
    }

    public void paintQubit (float centX,
			    float centY,
			    float len,
			    float theta,
			    float phi,
			    String label,
			    boolean eigenLabels) {

	// Draw the Spin Circle
	g2.setPaint (circCol);
	g2.drawOval (Math.round(centX-len-1), 
		     Math.round(centY-len-1),
		     Math.round(2*len+2),
		     Math.round(2*len+2));
	
	// Draw the Labels
	/*
	g2.setPaint (fontCol);
	g2.drawString (label, Math.round
		       (centX-font.getStringBounds
			(label,frc).getWidth()/2),
		       Math.round(centerY+spinLength+
				  2.5*font.getStringBounds(label,frc).getHeight()));
	*/
	label = "|0>";
	
	if (eigenLabels) {
	    float fx=(float)font.getStringBounds(label,frc).getWidth();
	    float fy=(float)font.getStringBounds(label,frc).getHeight();
	    g2.drawString ("|0>",
			   Math.round(centX+(len)*thetaPhiToX(0,0)-fx/2),
			   Math.round(centY-(len+fy)*thetaPhiToY(0,0)+fy/4));
	    g2.drawString ("|1>",
			   Math.round(centX+(len)*thetaPhiToX(pi,0)-fx/2),
			   Math.round(centY-(len+fy)*thetaPhiToY(pi,0)+fy/4));
	}
	
	// Draw the Axes and Projections onto Axes
	g2.setPaint (axesCol);
	g2.drawLine (Math.round(centX),
		     Math.round(centY),
		     Math.round(centX+len*thetaPhiToX(0,0)),
		     Math.round(centY-len*thetaPhiToY(0,0)));
	g2.drawLine (Math.round(centX),
		     Math.round(centY),
		     Math.round(centX+len*thetaPhiToX(pi/2,0)),
		     Math.round(centY-len*thetaPhiToY(pi/2,0)));
	g2.drawLine (Math.round(centX),
		     Math.round(centY),
		     Math.round(centX+len*thetaPhiToX(pi/2,pi/2)),
		     Math.round(centY-len*thetaPhiToY(pi/2,pi/2)));
	
	// Draw the Equator and Guide Latitude
	g2.setPaint (equatorCol);
	drawLatitude (g2, centX, centY, len, pi/2);
	g2.setPaint (guideCol);
	drawLatitude (g2, centX, centY, len, theta);
	
	// Draw the Equatorial Projection and Connecting Line
	g2.setPaint (circCol);
	g2.drawLine (Math.round(centX),
		     Math.round(centY),
		     (int)Math.round(centX+
				     Math.sin(theta)*len*thetaPhiToX(pi/2,phi)),
		     (int)Math.round(centY-
				     Math.sin(theta)*len*thetaPhiToY(pi/2,phi)));
	g2.setStroke (new BasicStroke(1f, BasicStroke.CAP_BUTT, 
				      BasicStroke.JOIN_MITER, 1f, dotline, 0f));
	g2.setPaint (equatorCol);
	g2.drawLine (Math.round(centX+len*thetaPhiToX(theta,phi)),
		     Math.round(centY-len*thetaPhiToY(theta,phi)),
		     (int)Math.round(centX+
				     Math.sin(theta)*len*thetaPhiToX(pi/2,phi)),
		     (int)Math.round(centY-
				     Math.sin(theta)*len*thetaPhiToY(pi/2,phi)));
	g2.setStroke (new BasicStroke());
	
	// Draw the |0> and |1> reference points
	g2.setPaint (zeroCol);
	g2.fillOval (Math.round(centX+len*thetaPhiToX(0f,0f)-eigenRad),
		     Math.round(centY-len*thetaPhiToY(0f,0f)-eigenRad),
		     2*eigenRad, 2*eigenRad);
	g2.setPaint (oneCol);
	g2.fillOval (Math.round(centX+len*thetaPhiToX(pi,0)-eigenRad),
		     Math.round(centY-len*thetaPhiToY(pi,0)-eigenRad),
		     2*eigenRad, 2*eigenRad);
	
	// Draw the Vector
	g2.setPaint (spinCol);
	g2.setStroke (new BasicStroke(2));
	g2.drawLine (Math.round(centX),
		     Math.round(centY),
		     Math.round(centX+len*thetaPhiToX(theta,phi)),
		     Math.round(centY-len*thetaPhiToY(theta,phi)));
	g2.setStroke (new BasicStroke());
    }

    public void paint (Graphics g) {
	paintWrapper (g);
    }

    public void paintWrapper (Graphics g) {
	int n;
	String ax;

	g2 = (Graphics2D) g;

	// Draw the background window
	g2.setPaint (backCol);
	g2.fillRect (0,0,panXSize-1,panYSize-1);

	// Draw the Axes and Labels
	g2.setPaint (fontCol);
	g2.drawLine (Math.round(axX),
		     Math.round(axY),
		     Math.round(axX+axLength*thetaPhiToX(0,0)),
		     Math.round(axY-axLength*thetaPhiToY(0,0)));
	g2.drawLine (Math.round(axX),
		     Math.round(axY),
		     Math.round(axX+axLength*thetaPhiToX(pi/2,0)),
		     Math.round(axY-axLength*thetaPhiToY(pi/2,0)));
	g2.drawLine (Math.round(axX),
		     Math.round(axY),
		     Math.round(axX+axLength*thetaPhiToX(pi/2,pi/2)),
		     Math.round(axY-axLength*thetaPhiToY(pi/2,pi/2)));

        font = new Font("SansSerif",Font.PLAIN,fontSize);
        g2.setFont (font);
        frc = g2.getFontRenderContext();

        ax = "x";
        float fx=(float)font.getStringBounds(ax,frc).getWidth();
        float fy=(float)font.getStringBounds(ax,frc).getHeight();
        g2.drawString ("z",Math.round(axX+(axLength+2*fx)*thetaPhiToX(0,0)-fx),
                       Math.round(axY-(axLength+2*fx)*thetaPhiToY(0,0)));
        g2.drawString ("x",Math.round(axX+(axLength+2*fx)*thetaPhiToX(pi/2,0)-fx),
                       Math.round(axY-(axLength+2*fx)*thetaPhiToY(pi/2,0)));
        g2.drawString ("y",Math.round(axX+(axLength+2*fx)*thetaPhiToX(pi/2,pi/2)-fx),
                       Math.round(axY-(axLength+2*fx)*thetaPhiToY(pi/2,pi/2)));
		 
	System.out.println("We got objects "+numberOfQubitObjects);
	for (n=0; n<numberOfQubitObjects; n++) {
	    System.out.println("calling paintWidget for object "+n);
	    if (!parentFrame.ready)
		System.out.println("not ready");
	    if (parentFrame.ready) {
		parentFrame.q[n].paintWidget();
		System.out.println("ready");
	    }
	}
    }
    
}

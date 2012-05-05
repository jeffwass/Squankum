Squankum
========

Simulation of Quantum Computation

Welcome to Squankum
--------
Currently Squankum lets you :

* Visualize a single qubit
    * Observe graphically the representation on a Bloch Sphere
    * Observe the relationship between the Bloch angular representation and spinor notation
* Perform single-qubit operations
* Observe representation of two coupled qubits (not fully implemented)

Screenshot
--------
Hadamard operation on a single qubit.  

![Hadamard operation screenshot](https://github.com/jeffwass/Squankum/raw/master/Screenshot.png)

* The qubit's state is represented as the yellow vector on the Bloch Sphere.
* It is expressed mathematically through the two angles theta/phi, or as the 2-element complex spinor below.
    * Relative probabilities for finding the qubit in state |0> and |1> are shown to the right of the spinor
* Sliders for theta / phi on the input qubit can be dragged, updating the input/output qubits in real time
    * A slider for an overall _phase_ is supplied.  This doesn't affect the vector, but scales the spinor by 
      exp^(_i phase_).  The qubit's state is invariant to this overall phase.
* Other single-qubit operators can be chosen from the drop-down menu
* The viewing angle can be adjusted through the alpha/beta/gamma sliders, which are implemented as [Eulerian angle](http://en.wikipedia.org/wiki/Euler_angles) rotations


History of Squankum
--------
In 2003-2004 I developed an interactive Quantum Computation applet, as part of my Technology Fellowship from the [Johns Hopkins Center for Educational Resources](http://www.cer.jhu.edu). There was a fair amount of interest in this project, so in 2007 I renamed it as Squankum, and released the source code as Free Open Source Software under the GNU Public License.

Why did you choose the name Squankum?
--------

Firstly, Squankum sounds cool, something roughly like an abbreviation for Simulation of Quantum Computation.

But more realistically, Squankum is an old name for a town nearby Freehold, New Jersey where I grew up. Squankum itself is a name in the Algonquian language meaning "Place of Evil Ghosts", given to the area by the Native American Lenni-Lenape tribe. They were possibly referring to the mosquitos. 
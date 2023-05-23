#  <p align ="center"> 🚚🏢📦 Courier Company Simulation with OOP Principles, Design Patterns, and Multi-threading 🏢📦🚚</p>

## <p align ="center"> This Java project showcases the implementation of object-oriented programming (OOP) principles, design patterns, and multi-threading to simulate the operations of a courier company. The system consists of branches, a sorting center, and a head office, with distribution vehicles associated with each branch.</p>
 
<br> 

### <p align ="center"> Implemented using: </p>
<p align ="center">
<a href="https://www.java.com/en/" target="_blank" rel="noreferrer">   <img src="https://static.vecteezy.com/system/resources/previews/022/100/686/non_2x/java-logo-transparent-free-png.png" width="80" height="80" /></a></p>
 
🏢 Branches have 🚐 van-type vehicles, while the sorting center has 🚚 "standard truck" type vehicles and an additional 🚛 "non-standard truck" type vehicle for transporting unusual cargo. The system creates a 📦 "package" and associates it with the appropriate branch when a shipment needs to be made.

<br>

## 📦 Packages are handled based on their type:

  - For small and standard packages: The main office associates the package with a local branch based on the sender's address. A van-type vehicle from the branch picks up the package and delivers it to the branch. A standard truck from the sorting center collects the package from the branch and transports it to the sorting center. Another sorting center truck then takes the package to the destination branch according to the recipient's address. Finally, a delivery vehicle from the destination branch delivers the package to the customer.

  - For non-standard packages: The main office associates the package with the sorting center. If the package size fits the non-standard truck, the truck directly collects the package from the sender and delivers it to the recipient.

Throughout the package's journey between customers, vehicles, branches, and the sorting center, the package's status changes, and a record of its transfer history (tracking) is maintained.

<br>

## 🎯 Project Goals:
  - Implement a graphical user interface (GUI) to build the delivery system and run the simulation.
  - Support parallel execution using multi-threading to enable simultaneous operation of branches and vehicles.
  - Display all branches, the sorting center, sender and recipient addresses, and various types of trucks moving between the system components.
  - Represent the movement of trucks and branches using animations and visualizations.
  - Enable pausing and resuming the simulation, as well as accessing information about packages and branches.
  - Implement additional features like package tracking, cloning branches, restoring system states, and generating reports.

<br>

## ⚙️ Technologies and Concepts:
  - OOP principles, design patterns, and multi-threading.
  - Singleton design pattern with double-checked locking (DCL).
  - Listener design pattern.
  - Thread pooling for client threads.
  - ReadWriteLock.
  - Prototype design pattern for branch duplication.
  - Memento design pattern for system state restoration.

<br>

## 📊 Visualization:
  - The simulation interface presents a visual representation of the delivery system, with branches and trucks displayed as animated entities.
  - Each package is represented by red circles for the sender and recipient addresses, connected by lines to the corresponding branches or sorting center.
  - Van-type trucks are represented by dark blue squares, and standard trucks by light/dark green squares, indicating their status and the number of packages they carry.
  - Non-standard trucks are displayed as light/dark red squares, based on whether they are empty or carrying a package.
  - The simulation includes controls to stop, resume, and view package and branch information.

<br>

## 🔧 Additional Features:
  - Package creation and tracking using a Customer class.
  - Package delivery status updates and tracking recorded in a txt.tracking file.
  - Multi-threaded implementation for branch handling and vehicle coordination.
  - Graphical representation of package movement, vehicle routes, and address connections.
  - Menu options for cloning branches, restoring system states, and generating reports.

<br>

## 🖼️ Sample images:
<br>

### Image No.1

| ![Image-1.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-1.JPG?raw=true) | 
|:--:| 
| *1.	The image displays a courier company simulation with 5 branches, 5 vehicles, and 8 packages. The branches are represented by animated entities, and there are red circles indicating the sender and recipient addresses of the packages. The trucks and their routes are not yet visible in this initial state of the simulation.* |

<br>

### Image No.2

| ![Image-2.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-2.JPG?raw=true) | 
|:--:| 
| *2.	The simulation has started running, and the image shows the dynamic movement of vehicles and packages within the system. The branches, trucks, and packages are in motion, depicting the ongoing operations of the courier company.* |

<br>

### Image No.3

| ![Image-3.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-3.JPG?raw=true) | 
|:--:| 
| *3.	After adding all 8 packages to the system, the image presents an overview of the entire delivery system. The branches, sorting center, and vehicles are all visible, with packages represented by red circles connected by lines to their corresponding branches or the sorting center.* |

<br>

### Image No.4

| ![Image-4.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-4.JPG?raw=true) | 
|:--:| 
| *4.	As time progresses, the system shows the initial rush of normal packages to the branches. Some packages are being transferred from the branches to the sorting center using standard trucks, which are now depicted with a dark green color. The number displayed above the truck represents the number of packages it carries. Additionally, a non-standard truck loaded a package at the sender's location and is currently on its way to deliver the it to the customer. The color of this truck has changed to dark red.* |

<br>

### Image No.5

| ![Image-5.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-5.JPG?raw=true) | 
|:--:| 
| *5.	In this image, some packages have been transported from the sorting center to the customer's branches using standard trucks. The trucks are visible, and the delivery of packages from the branches to the customers has commenced, indicated by the presence of van-type trucks in the system.* |

<br>

### Image No.6

| ![Image-6.JPG](https://github.com/NadavIs56/Java_Delivery_Management_Simulation/blob/main/Sample%20images/Image-6.JPG?raw=true) | 
|:--:| 
| *6.	The final picture illustrates a scenario where all packages have been successfully delivered to the customers. The system shows that all packages have reached their destinations, and the delivery process is complete.* |

<br>

## <p align ="center"> 🚀 In summary, this project offers a comprehensive simulation of a courier company's operations, showcasing proficiency in Java, object-oriented programming, design patterns, multi-threading, and graphical user interface development. 🚀</p>

## <p align ="center">✨ Enjoy exploring this exciting project, visualizing the operations of a courier company in action! ✨</p>

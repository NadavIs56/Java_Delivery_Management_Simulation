#  <p align ="center"> Java_Delivery_Management_Simulation</p>

##  <p align ="center"> 🚚🏢📦 Courier Company Simulation with OOP Principles, Design Patterns, and Multi-threading 🏢📦🚚</p>

<p align ="center"> This Java project showcases the implementation of object-oriented programming (OOP) principles, design patterns, and multi-threading to simulate the operations of a courier company. The system consists of branches, a sorting center, and a head office, with distribution vehicles associated with each branch.</p>
  

🏢 Branches have van-type vehicles, while the sorting center has "standard truck" type vehicles and an additional "non-standard truck" type vehicle for transporting unusual cargo. The system creates a "package" and associates it with the appropriate branch when a shipment needs to be made.

📦 Packages are handled based on their type:

For small and standard packages: The main office associates the package with a local branch based on the sender's address. A van-type vehicle from the branch picks up the package and delivers it to the branch. A standard truck from the sorting center collects the package from the branch and transports it to the sorting center. Another sorting center truck then takes the package to the destination branch according to the recipient's address. Finally, a delivery vehicle from the destination branch delivers the package to the customer.

For non-standard packages: The main office associates the package with the sorting center. If the package size fits the non-standard truck, the truck directly collects the package from the sender and delivers it to the recipient.

Throughout the package's journey between customers, vehicles, branches, and the sorting center, the package's status changes, and a record of its transfer history (tracking) is maintained.


🎯 Project Goals:
Implement a graphical user interface (GUI) to build the delivery system and run the simulation.
Support parallel execution using multi-threading to enable simultaneous operation of branches and vehicles.
Display all branches, the sorting center, sender and recipient addresses, and various types of trucks moving between the system components.
Represent the movement of trucks and branches using animations and visualizations.
Enable pausing and resuming the simulation, as well as accessing information about packages and branches.
Implement additional features like package tracking, cloning branches, restoring system states, and generating reports.


⚙️ Technologies and Concepts:
OOP principles, design patterns, and multi-threading.
Singleton design pattern with double-checked locking (DCL).
Listener design pattern.
Thread pooling for client threads.
ReadWriteLock.
Prototype design pattern for branch duplication.
Memento design pattern for system state restoration.


📊 Visualization:
The simulation interface presents a visual representation of the delivery system, with branches and trucks displayed as animated entities.
Each package is represented by red circles for the sender and recipient addresses, connected by lines to the corresponding branches or sorting center.
Van-type trucks are represented by dark blue squares, and standard trucks by light/dark green squares, indicating their status and the number of packages they carry.
Non-standard trucks are displayed as light/dark red squares, based on whether they are empty or carrying a package.
The simulation includes controls to stop, resume, and view package and branch information.


🔧 Additional Features:
Package creation and tracking using a Customer class.
Package delivery status updates and tracking recorded in a txt.tracking file.
Multi-threaded implementation for branch handling and vehicle coordination.
Graphical representation of package movement, vehicle routes, and address connections.
Menu options for cloning branches, restoring system states, and generating reports.

🚀 In summary, this project offers a comprehensive simulation of a courier company's operations, showcasing proficiency in Java, object-oriented programming, design patterns, multi-threading, and graphical user interface development. 🚀

✨ Enjoy exploring this exciting project, visualizing the operations of a courier company in action! ✨

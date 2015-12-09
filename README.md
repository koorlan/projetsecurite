# projetsecurite

##Achivements
Syntax of commands \[ManagerMethod] [Manager] \[Arg1] .. [ArgN] Only support 2 ATM and need tothnik more cleverly of parsing especially for non string args

Note: Manager methods are directly Mapped to terminal assuming you initialize manager parsing in Core.initCommands() ..it's testing code at least we need to find better way to store online's managers

Many parts are missing but if you want lulz...

.start server [enter]   //Randomized port.

.setName user Gilles [enter]
.getName user [enter]
.setAssignement user...ect

FORGE WILL NOT WORK ANYMORE
In cause: PacketManager now check GET/POST and then integrity of the object after (RequestModel for GET) so.. forge with jsut string is impossible. See below
.forge packet {POST or GET} hello [enter]  //maybe recheck the model of a packet but think globally packetModel.Content is a byte array so we can serialize other object on it ;). More Precisely we maybe need to tag this packet .. or note , just the gate(frontale..je sais pas trop comment l'appeller en english) need to do that.
.send packet 123123(port your server choose previously) [enter]

Update Testing server >> .group request INSA <port> [enter]

Explication:
packetManager is used to build and process in/out packet.
.forge save automatically the packet he has succefully builded to packetModel as temp
forge() obviously return the packet he builded so we can forge on the fly

.send take the last packetModel packet saved and send (on the fly socket) it to the specified port. (at least here get user frontal). Don't wait answer .. maybe need to change that after .. or create a poolpacket waiting..

Note: EOF boolean in packet is needed to tell the reciever thread to close the stream and the socket. (maybe antoher method?)

quit the program : .stop terminal
TODO close cleanly all manager and dependecies.

##Todo
See some todos on the source file..many missings

list of thinking but no time atm

* At management of module if they are not here (=null) cause others try to load method from a null pointer is we have not instantiate and liked it to the core ( Ex: .getLogManager.log(this,"chabada" ... if getLogManager return null !?? )

##Tips
Dirty management of closing thread methods.. often javaw dosen't kill itself. If you need to kill all deamons running.

* Windows >> taskkill /F /IM javaw.exe
* Linux >> pkill recursive...

##Ideas

* Encapsulate packet into a Securepacket. only Secured packet are on the network.
if ok >> move forge ? process ? send ? and use packetManager a Deserializer and ressource ochester ? ?

*Use apache serialization utils tool.
##Other stuff

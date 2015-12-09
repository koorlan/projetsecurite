# projetsecurite

##Achivements
Syntax of commands \[ManagerMethod] [Manager] \[Arg1] .. [ArgN] Only support 2 ATM and need tothnik more cleverly of parsing especially for non string args

Note: Manager methods are directly Mapped to terminal assuming you initialize manager parsing in Core.initCommands() ..it's testing code at least we need to find better way to store online's managers

Many parts are missing but if you want lulz...

.start server [enter]   //Randomized port.

.setName user Gilles [enter]
.getName user [enter]
.setAssignement user...ect

.forge request {POST or GET} hello [enter]  //maybe recheck the model of a request but think globally RequestModel.Content is a byte array so we can serialize other object on it ;). More Precisely we maybe need to tag this packet .. or note , just the gate(frontale..je sais pas trop comment l'appeller en english) need to do that.

.send request 123123(port your server choose previously) [enter]

Explication: 
RequestManager is used to build and process in/out packet.
.forge save automatically the request he has succefully builded to RequestModel as temp
forge() obviously return the request he builded so we can forge on the fly

.send take the last RequestModel request saved and send (on the fly socket) it to the specified port. (at least here get user frontal). Don't wait answer .. maybe need to change that after .. or create a poolRequest waiting..

Note: EOF boolean in request is needed to tell the reciever thread to close the stream and the socket. (maybe antoher method?)

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

* Encapsulate Request into a SecureRequest. only Secured request are on the network.
if ok >> move forge ? process ? send ? and use RequestManager a Deserializer and ressource ochester ? ?

##Other stuff
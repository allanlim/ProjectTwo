# ProjectOne
Project one for Distributed Computing and Networks at UNF

## Running the software
To run the software ensure that Java 1.8 is installed on your machine.

This is a command line application which pings a specified server for information via sockets. 

Specify a number which corresponds to menu options and specify a hostname.
For instance:

`1 -hostname 127.0.0.1`

If there is invalid input the program will notify you can make the necessary adjustments. 

## Version

1.0.0
- Initial Release

1.0.1
- Bug fixes enhances to sockets 
- Refactor of Server class

1.1.0 
- Created Server handler class to spin up new threads when clients connect 
- Refactored main class to create clients in new threads before running their threads

1.1.1 
- Updates to Server Handler
- Performance Improvements
- Bug Fixes

1.1.2 
- Server Handler now corrrectly creates threads
- threads are all created before they are executed
- Server listens for new connections continuously instead of stopping after the first client

## FAQ 

### Whats the difference between this and ProjectOne?

This project includes a Server Handler class which is multi threaded in nature. This server (unlike the server from project one) will create new threads when clients connect and can delegate a process to handle the client while the main server thread listens for more clients.

#### It keeps saying invalid input whats wrong? 

Ensure that you have the hostname flag set using `-hostname` then a space character and finally your desired hostname.
Also be sure that you have selected a valid number from the menu 1-7 

#### Why do I see five clients? 

By default the loop which creates the client threads is set to 5. It will initialize 5 clients in different threads handled by the Client handler class before executing them all at once.



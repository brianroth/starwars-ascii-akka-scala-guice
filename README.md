#What is this???
This project will create animated ascii console art from any gif.

check out the web-app-conversion branch to use this app as a web server.

It is written in scala and uses akka actors for asynchronous processing, as well
as google guice for dependency injection

#How to run???

`sbt run`

will run with default values.

`sbt "run --help"`

will print out help information

There are three arguments, width, height, and file. Here is an example:

`sbt "run --height 100 --width 150 --file /home/dgoetsch/Dowloads/bb8.gif"`

height and width are the number of ascii characters to render in the given direction
file is a path to a file ont he system, or a gif packaged in this project.

You can run packaged gifs by just providing the name. Options will print with help.

Example:

`sbt "run --file bb8.gif"`


If you don't have sbt installed, you can use the activator script

`./activator "run --file bb8.gif"`

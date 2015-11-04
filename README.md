#What is this???
This project will create animated ascii console art from any gif.

It is written in scala and uses akka actors for asynchronous processing, as well
as google guice for dependency injection

#How to run???

`sbt run`

will print out usage.

There are two arguments, targetWidth and file. Here is an example:

`sbt "run 150 /home/dgoetsch/Dowloads/bb8.gif"`

I've also put a bunch of gifs in resources, and you can run these by just
providing the name. They will print with usage.

Example:

`sbt "run 150 bb8.gif"`


If you don't have sbt installed, you can use the activator script

`./activator run 150 bb8.gif`

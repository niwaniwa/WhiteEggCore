/*!
 * example
 * create by niwaniwa
 */
whiteegg.on('command', function(event) {
  Console.println('Command : ' + event.getMessage());
  java.lang.System.exit(0);
});

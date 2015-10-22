/*!
 * create by niwaniwa
 */
var white =(function() {
  var white = function(){
    this.map = new java.util.HashMap();
  };

  var p = white.prototype;

  p.call = function call(eventName, event) {
    var array = this.map.keySet().toArray();

    for (var i = 0; i < this.map.keySet().size(); i++) {
      var key = array[i];
      if(key.toString().equalsIgnoreCase(eventName)){
        this.map.get(key)(event);
      }
    }

  };

  p.on = function on(eventName, event) {
    this.map.put(eventName, event);
  };

  p.off = function off(eventName) {
    this.map.remove(eventName);
  };

  return white;

})();
var whiteegg = new white();

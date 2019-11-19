var DropdownRow = function(item, opts){

  var self = this;

  var el = self.el = $('<div class="tagbox-item" />');

  if(opts['itemClass']) el.addClass(opts['itemClass']);

  self.item = item;

  var format = opts['rowFormat'];

  if(typeof format == 'string'){
    if(typeof item == 'string') item = { value: item };
    el.html(format.replace(/\{\{([^}]*)\}\}/g, function(match, field){
      return item[field];
    }));
  }else{
    el.html(format(item));
  }

  self.select = function(){
    el.addClass('selected');
  };

  self.deselect = function(){
    el.removeClass('selected');
  };

};

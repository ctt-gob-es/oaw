var Token = function(item, opts){

  var self = this;

  var el = self.el = $('<div class="tagbox-token">' +
                          '<span></span>' +
                          '<a>&times;</a>' +
                        '</div>')
                        .data('token', self);

  var format = opts['tokenFormat'];

  self.value = (typeof item == 'string') ? item : item[opts['valueField']];
  self.item = item;


  if(typeof format == 'string'){
    if(typeof item == 'string') item = { value: item };
    el.children('span').html(format.replace(/\{\{([^}]*)\}\}/g, function(match, field){
      return item[field];
    }));
  }else{
    el.children('span').html(format(item));
  }

  self.remove = function(){
    el.data('token', null);
    el.remove();
    self.item = null;
    self.el = null;
  };

  self.select = function(){
    el.addClass('selected');
  };

  self.deselect = function(){
    el.removeClass('selected');
  };
};

$.fn['tagbox'] = function(opts){

  var defaults = {
    'maxHeight': 200,
    'maxListItems': 20,
    'rowFormat': '{{value}}',
    'tokenFormat': '{{value}}',
    'valueField': 'value',
    'searchIn': ['value'],
    'delimiter': ',',
    'allowDuplicates': false,
    'createNew': function(txt){ return { value: txt }; },
    'allowNew': false,
    'newText': '{{txt}}',
    'emptyText': 'Not Found',
    'dropdownContainer': 'body',
    'maxItems': -1,
    'autoShow': false
  };

  var options = $.extend({}, defaults, opts);

  return this.each(function(){
    if($(this).data('tagbox')) return;

    var tb = new TagBox(this, options);
    $(this).data('tagbox', tb);
  });

};

var CompletionDropdown = function(tagbox, opts){

  var self = this;

  var selectedRow, selectedIndex, rows;

  var el = self.el = $('<div class="tagbox-dropdown"><div class="tagbox-list"></div></div>')
    .css({
      maxHeight: opts['maxHeight']
    })
    .hide();

  if(opts['allowNew']){
    var newRow = $('<div class="tagbox-item new-item" />').prependTo(el)
      .on('mousedown', function(){
        tagbox.dontHide = true;
      }).on('mouseup', function(){
        tagbox.dontHide = false;
      }).on('click', function(){
        tagbox.addToken(opts['createNew'](self.newText));
      });
  }

  self.updatePosition = function(input){
    var o1 = el.offset();
    var o2 = input.offset();
    var o3 = el.parent().offset();

    if(el.parent().is('body')){
      // TODO figure out how to take into account margin/padding on body element properly
      o3 = {
        top: 0,
        left: 0
      };
    }

    el.css({
      top: o2.top - o3.top + input.height() + 1,
      left: o2.left - o3.left,
      width: input.outerWidth()
    });
  };

  self.show = function(){
    el.show();
    self.visible = true;
  };

  self.hide = function(){
    el.hide();
    self.visible = false;
  };

  self.getSelected = function(){
    if(selectedRow) return selectedRow.item;
  };

  self.selectNext = function(){
    if(selectedIndex === rows.length - 1){
      if((opts['allowNew'] && self.newText) || rows.length === 0){
        selectRow(-1);
      }else{
        selectRow(0);
      }
    }else{
      selectRow(selectedIndex + 1);
    }
  };

  self.selectPrevious = function(){
    if(selectedIndex === 0){
      if(opts['allowNew'] && self.newText){
        selectRow(-1);
      }else{
        selectRow(rows.length - 1);
      }
    }else{
      if(selectedIndex === -1) selectedIndex = rows.length;
      selectRow(selectedIndex - 1);
    }
  };

  var scrollTimeout;

  function selectRow(idx, scrollWithTimeout){
    if(selectedRow){
      selectedRow.deselect();
    }
    newRow && newRow.removeClass('selected');
    if(typeof idx !== 'number'){
      idx = rows.indexOf(idx);
    }
    if(idx >= 0){
      selectedRow = rows[idx];
      selectedRow.select();
      clearTimeout(scrollTimeout);
      if(scrollWithTimeout){
        scrollTimeout = setTimeout(function(){
          scrollToRow(selectedRow.el);
        }, 80);
      }else{
        scrollToRow(selectedRow.el);
      }
    }else{
      selectedRow = false;
      newRow && newRow.addClass('selected');
    }
    selectedIndex = idx;
  }

  function scrollToRow(r){
    var o = r.offset().top - el.offset().top;
    if(o < 0){
      el.scrollTop(o + el.scrollTop());
    }else if(o > el.innerHeight() - r.outerHeight()){
      el.scrollTop(o + el.scrollTop() - el.innerHeight() + r.outerHeight());
    }
  }

  self.setEmptyItem = function(txt){
    if(!newRow) return;
    self.newText = txt;
    if(!txt){
      newRow.hide();
    }else{
      newRow.show().text(opts['newText'].replace(/\{\{txt\}\}/g, txt));
    }
  };

  self.showItems = function(items){
    el.find('.tagbox-list').empty();
    rows = [];

    if(items.length > 0){
      for(var i = 0; i < Math.min(items.length, opts['maxListItems']); i += 1){
        var row = new DropdownRow(items[i], opts);
        row.el.appendTo(el.find('.tagbox-list'));
        row.el.on('mouseover', function(row){ return function(){
          selectRow(row, true);
        }; }(row));
        row.el.on('mousedown', function(){
          tagbox.dontHide = true;
        }).on('mouseup', function(){
          tagbox.dontHide = false;
        });
        row.el.on('click', function(item){ return function(){
          tagbox.addToken(item);
        }; }(items[i]));
        rows.push(row);
      }
      selectRow(0);
    }else if(!opts['allowNew']){
      $('<div class="tagbox-item empty"/>')
        .text(opts['emptyText'])
        .appendTo(el.find('.tagbox-list'));
      selectRow(-1);
    }else{
      selectRow(-1);
    }

    self.show();
  };
};

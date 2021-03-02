// Register the related commands.
FCKCommands.RegisterCommand( 'TAW', new FCKDialogCommand( FCKLang['DlgTawTitle'] , FCKLang['DlgTawTitle'] ,  FCKConfig.PluginsPath +'taw/taw.html' , 600, 500 ) ) ;

// Create the "taw" toolbar button.
var oFindItem		= new FCKToolbarButton( 'TAW', FCKLang['DlgTawTitle'] ) ;
oFindItem.IconPath	= FCKConfig.PluginsPath + 'taw/taw.png' ;

FCKToolbarItems.RegisterItem( 'taw', oFindItem ) ;			// 'taw' is the name used in the Toolbar config.




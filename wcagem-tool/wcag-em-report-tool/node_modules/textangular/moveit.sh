rm  -Rf ../Tracker2/client/bower_components/textAngular/*
cp -R * ../Tracker2/client/bower_components/textAngular
cd ..
tar -zcf textAngular.tar.gz textAngularMain
mv textAngular.tar.gz Tracker2/textAngularMain.tar.gz


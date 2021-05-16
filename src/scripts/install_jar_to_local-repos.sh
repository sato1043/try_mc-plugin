#! /bin/bash -x

rm -rf ./install_jar_tmp

mkdir install_jar_tmp || exit 1

cd install_jar_tmp || exit 1

rm -rf libby

git clone https://github.com/AlessioDP/libby.git

cd libby || exit 1

rm -rf .git

mvn package

cd ..

find ./libby -name "*.jar" -exec cp {} . \;

function install_jar {
  [ -f $1 ] || exit 1
  mvn install:install-file \
     -Dfile=$1 \
     -DgroupId=$2 \
     -DartifactId=$3 \
     -Dversion=$4 \
     -Dpackaging=$5 \
     -DgeneratePom=true
}

[ -f libby-core-1.1.0.jar ] && install_jar libby-core-1.1.0.jar net.byteflux libby-core 1.1.0 jar
[ -f libby-slf4j-1.1.0.jar ] && install_jar libby-slf4j-1.1.0.jar net.byteflux libby-slf4j 1.1.0 jar
[ -f libby-bukkit-1.1.0.jar ] && install_jar libby-bukkit-1.1.0.jar net.byteflux libby-bukkit 1.1.0 jar

cd ..

exit 0

#! /bin/bash
#
# usage: ./CREATE_DATABASE.sh (production|staging|testing) [db_sign] [hostname] [passhash]
#
# To ensure to exist a user:
#   $ mysql -u mysql_user -p -e 'USE db_name; SELECT 1'

PHASE=z
[ z"$1" = zproduction ] && PHASE=production
[ z"$1" = zstaging ] && PHASE=staging
[ z"$1" = ztesting ] && PHASE=testing
[ z = ${PHASE} ] && exit 1

PROPERTIES=../../.${PHASE}.properties
[ -f "${PROPERTIES}" ] || exit 2

function get_property {
    grep "^$2\\s*=\\s*" "$1" | cut -d'=' -f2 | sed 's/^ *//'
}

MYSQL_HOST=$(get_property $PROPERTIES "storage.mysql.host")
MYSQL_PORT=$(get_property $PROPERTIES "storage.mysql.port")
MYSQL_USER=$(get_property $PROPERTIES "storage.mysql.user")
MYSQL_PASSWORD=$(get_property $PROPERTIES "storage.mysql.password")
MYSQL_DB_NAME=$(get_property $PROPERTIES "storage.mysql.db_name")
[ -z "${MYSQL_HOST}" ] && exit 3
[ -z "${MYSQL_PORT}" ] && exit 3
[ -z "${MYSQL_USER}" ] && exit 3
[ -z "${MYSQL_PASSWORD}" ] && exit 3
[ -z "${MYSQL_DB_NAME}" ] && exit 3

cat <<END_OF_DATA #| sudo mysql
CREATE DATABASE ${MYSQL_DB_NAME} DEFAULT CHARACTER SET utf8;
GRANT ALL PRIVILEGES ON ${MYSQL_DB_NAME}.* TO '${MYSQL_USER}'@'${MYSQL_HOST}';
FLUSH PRIVILEGES;
END_OF_DATA

#__END__

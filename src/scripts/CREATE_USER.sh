#! /bin/bash
#
# CREATE_USER.sh (production|staging|testing)
#
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
MYSQL_USER=$(get_property $PROPERTIES "storage.mysql.user")
MYSQL_PASSWORD=$(get_property $PROPERTIES "storage.mysql.password")
[ -z "${MYSQL_HOST}" ] && exit 3
[ -z "${MYSQL_USER}" ] && exit 3
[ -z "${MYSQL_PASSWORD}" ] && exit 3

cat <<END_OF_DATA #| sudo mysql
CREATE USER '${MYSQL_USER}'@'${MYSQL_HOST}' IDENTIFIED WITH mysql_native_password BY '${MYSQL_PASSWORD}';
SELECT user,authentication_string,plugin,host FROM mysql.user WHERE user = '${MYSQL_USER}';
END_OF_DATA

#__END__

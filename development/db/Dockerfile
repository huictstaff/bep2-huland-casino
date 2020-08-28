FROM postgres:12-alpine

COPY ./create_users.sh   /docker-entrypoint-initdb.d/10-create_users.sh
COPY ./create_dbs.sh     /docker-entrypoint-initdb.d/20-create_dbs.sh
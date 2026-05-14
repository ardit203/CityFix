#!/bin/bash

set -e

containers=$(docker ps -aq)
if [ -n "$containers" ]; then
  docker rm -f $containers
fi

images=$(docker images -aq)
if [ -n "$images" ]; then
  docker rmi -f $images
fi

volumes=$(docker volume ls -q)
if [ -n "$volumes" ]; then
  docker volume rm $volumes
fi

networks=$(docker network ls --format "{{.Name}}" | grep -vE "^(bridge|host|none)$")
if [ -n "$networks" ]; then
  docker network rm $networks
fi

docker system prune -a --volumes -f

echo "Docker cleanup complete."

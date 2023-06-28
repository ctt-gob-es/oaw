#!/bin/bash
tar -xzf ./mysql/database.tar.gz -C ./mysql
docker compose up -d

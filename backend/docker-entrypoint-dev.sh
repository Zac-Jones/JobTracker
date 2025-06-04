#!/bin/bash

# Development startup script for Spring Boot with hot reload and auto-compilation

echo "Starting Spring Boot development environment with auto-compilation..."

# Initial compilation
echo "Performing initial compilation..."
mvn compile -q

# File watching and compilation loop
echo "Starting file watcher for auto-compilation..."
find /app/src -name "*.java" -type f -exec md5sum {} \; | sort > /tmp/src_checksum_old

COMPILE_IN_PROGRESS=false

while true; do
    # Skip checking during compilation
    if [ "$COMPILE_IN_PROGRESS" = "true" ]; then
        sleep 2
        continue
    fi
    
    # Check for source file changes
    find /app/src -name "*.java" -type f -exec md5sum {} \; | sort > /tmp/src_checksum_new
    
    if ! cmp -s /tmp/src_checksum_old /tmp/src_checksum_new; then
        echo "Source changes detected - recompiling..."
        COMPILE_IN_PROGRESS=true
        
        mvn compile -q
        echo "Compilation complete."
        
        # Update reference checksum
        cp /tmp/src_checksum_new /tmp/src_checksum_old
        
        # Wait for DevTools restart
        echo "Waiting for DevTools restart..."
        sleep 8
        COMPILE_IN_PROGRESS=false
    fi
    
    sleep 3
done &

# Start Spring Boot with debug support
echo "Starting Spring Boot application with debug support..."
exec mvn spring-boot:run \
    -Dspring-boot.run.fork=false \
    -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'

#!/usr/bin/env bash

# Builds both apps, starts the SOAP server, waits for its WSDL, then starts the REST client.
#   ./run.sh --movies.soap.endpoint=http://localhost:9999/services/movies

set -euo pipefail

cd "$(dirname "$0")"

SERVER_WSDL="http://localhost:8080/services/movies?wsdl"
CLIENT_URL="http://localhost:8081/movies"
mkdir -p logs

SERVER_PID=""
CLIENT_PID=""


cleanup() {
    echo
    echo "Stopping..."
    [[ -n "$CLIENT_PID" ]] && kill "$CLIENT_PID" 2>/dev/null || true
    [[ -n "$SERVER_PID" ]] && kill "$SERVER_PID" 2>/dev/null || true
    wait 2>/dev/null || true
}
trap cleanup EXIT INT TERM

# wait_for <url> <pid> <name> <logfile>
wait_for() {
    local url=$1 pid=$2 name=$3 log=$4
    for _ in $(seq 1 60); do
        if ! kill -0 "$pid" 2>/dev/null; then
            echo "$name exited during startup. Last log lines:"
            tail -n 30 "$log"
            exit 1
        fi
        # -o /dev/null: any HTTP response (even 502) means the app is up
        if curl -s -o /dev/null "$url"; then
            echo "$name is up."
            return 0
        fi
        sleep 1
    done
    echo "$name did not start within 60s. Last log lines:"
    tail -n 30 "$log"
    exit 1
}

for port in 8080 8081; do
    rc=0
    curl -s -o /dev/null --max-time 2 "http://localhost:$port/" || rc=$?
    if [[ $rc -ne 7 ]]; then
        echo "Port $port is already in use - stop whatever is listening there first (e.g. lsof -nP -iTCP:$port)."
        exit 1
    fi
done

echo "Building (wsdl2java, compile, test)..."
./gradlew build

echo "Starting SOAP server on :8080 (logs/server.log)..."
java -jar server/build/libs/server.jar > logs/server.log 2>&1 &
SERVER_PID=$!
wait_for "$SERVER_WSDL" "$SERVER_PID" "SOAP server" logs/server.log


echo "Starting REST client on :8081 (logs/client.log)..."
java -jar client/build/libs/client.jar "$@" > logs/client.log 2>&1 &
CLIENT_PID=$!
wait_for "$CLIENT_URL" "$CLIENT_PID" "REST client" logs/client.log

cat <<EOF

Both apps are running. Try:
  curl -s '$CLIENT_URL' | head -c 400; echo
  curl -s '$CLIENT_URL?keyword=carpenter'
  curl -s '$CLIENT_URL?keyword=SLASHER'
  open '$SERVER_WSDL'

Press Ctrl+C to stop both.
EOF

wait

#!/bin/bash

allApps=("jamb" "waec" "beceJss" "putmeAbsu" "putmeAbsu" "putmeAbu" "putmeBuk" "putmeDelsu" "putmeEsut" "putmeFunaab" "putmeFuta" "putmeFutminna"
"putmeFuto" "putmeImsu" "putmeNda" "putmeOau" "putmeRsust" "putmeUi" "putmeUniabuja" "putmeUniben" "putmeUnical" "putmeUnijos" "putmeUnilag"
"putmeUnilorin" "putmeUniport" "putmeUnizik" "putmeUnn")

package_app() {
  local build="$1"
  echo "packaging $build app ..."
#  ( ./gradlew jpackage -Dbuild="$build" --stacktrace & ) ; sleep 30s
  ./gradlew jpackage -Dbuild="$build" --stacktrace
}

OPT_STRING="ae:o:"

while getopts ${OPT_STRING} opt; do
    case $opt in
        a)
          all=true
          ;;
        e)
          IFS=',' read -r -a exclude <<< "${OPTARG}"
          ;;
        o)
          IFS=',' read -r -a apps <<< "${OPTARG}"
          ;;
        :)
          echo "Option -${OPTARG} requires an argument."
          exit 1
          ;;
        ?)
          exit 1
        ;;
    esac
done

if [ $all ]; then
  apps=("${allApps[@]}")
elif [ "${#exclude[@]}" -gt 0 ]; then
  apps=()
  for app in "${allApps[@]}"; do
      if [[ "${exclude[*]}" =~ $app ]]; then
          continue
      else
        apps+=("$app")
      fi
  done
fi

for app in "${apps[@]}"; do
  package_app "$app"
done



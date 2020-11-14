#!/bin/sh

username=$1
password=$2

passwd $1 << EOF
$2
$2
EOF

echo "修改成功"

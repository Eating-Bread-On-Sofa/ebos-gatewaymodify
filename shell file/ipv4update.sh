#!/bin/sh

name=$1
ip=$2
gateway=$3
dns=$4

cd /etc/netplan
: > 50-cloud-init.yaml
cat >>50-cloud-init.yaml<< EOF
# This file is generated from information provided by the datasource.  Changes
# to it will not persist across an instance reboot.  To disable cloud-init's
# network configuration capabilities, write a file
# /etc/cloud/cloud.cfg.d/99-disable-network-config.cfg with the following:
# network: {config: disabled}
network:
  ethernets:
    $name:
      addresses:
      - $ip
      dhcp4: false
      gateway4: $gateway
      nameservers:
        addresses:
        - $dns
        search: []
  version: 2
EOF
netplan apply
echo "修改成功"

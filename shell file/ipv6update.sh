#!/bin/sh

name=$1
ipv4=$2
ipv6=$3
gateway4=$4
gateway6=$5
dns=$6

rm -rf /etc/netplan/50-cloud-init.yaml
cd /etc/netplan
touch 50-cloud-init.yaml
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
      - $ipv4
      - $ipv6
      dhcp4: false
      dhcp6: false
      gateway4: $gateway4
      gateway6: $gateway6
      nameservers:
        addresses:
        - $dns
        search: []
  version: 2
EOF
netplan apply
echo "修改成功"

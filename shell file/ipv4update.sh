#!/bin/sh

name=$1
ip=$2
netmask=$3
gateway=$4
ips=$5

cd /etc/network/interfaces.d
: > $name
cat >> $name  << EOF
#iface $name inet manual
auto $name
allow-hotplug $name
iface $name inet static
	address $ip
	netmask $netmask
	gateway $gateway
EOF
ifconfig $name $ips up
echo "修改成功"

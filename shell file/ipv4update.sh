#!/bin/sh

name=$1
ip=$2
netmask=$3
gateway=$4
ips=$5
dns=$6

rm -rf /etc/network/interfaces.d/$name
cd /etc/network/interfaces.d
touch $name
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
rm -rf /etc/resolv.conf
cd /etc
touch resolv.conf
cat >> resolv.conf << EOF
# This file is managed by man:systemd-resolved(8). Do not edit.
#
# This is a dynamic resolv.conf file for connecting local clients to the
# internal DNS stub resolver of systemd-resolved. This file lists all
# configured search domains.
#
# Run "systemd-resolve --status" to see details about the uplink DNS servers
# currently in use.
#
# Third party programs must not access this file directly, but only through the
# symlink at /etc/resolv.conf. To manage man:resolv.conf(5) in a different way,
# replace this symlink by a static file or a different symlink.
#
# See man:systemd-resolved.service(8) for details about the supported modes of
# operation for /etc/resolv.conf.

nameserver $dns
options edns0
EOF
echo "修改成功"

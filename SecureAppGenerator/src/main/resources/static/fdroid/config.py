#!/usr/bin/env python2

# Copy this file to config.py, then amend the settings below according to
# your system configuration.

# Path to the Android SDK
sdk_path = "$ANDROID_HOME"

# Path to various versions of the Android NDK
# Most users will have the latest at $ANDROID_NDK, which is used by default
# If a version is missing or assigned to None, it is assumed not installed
ndk_paths = {
    'r9b': None,
    'r10e': "$ANDROID_NDK"
}

# Build tools version to be used
build_tools = "22.0.1"

# Command or path to binary for running Ant
ant = "ant"

# Command or path to binary for running maven 3
mvn3 = "mvn"

# Command or path to binary for running Gradle
gradle = "gradle"

# Set the maximum age (in days) of an index that a client should accept from
# this repo. Setting it to 0 or not setting it at all disables this
# functionality. If you do set this to a non-zero value, you need to ensure
# that your index is updated much more frequently than the specified interval.
# The same policy is applied to the archive repo, if there is one.
repo_maxage = 0

repo_url = "https://s3.amazonaws.com/qa-martus-sag/fdroid/repo"
repo_name = "Secure App"
repo_icon = "fdroid-icon.png"
repo_description = """
This is a repository of apps to be used with F-Droid. Applications in this
repository are either official binaries built by the original application
developers, or are binaries built from source by the admin of f-droid.org
using the tools on https://gitlab.com/u/fdroid.
"""

# As above, but for the archive repo.
# archive_older sets the number of versions kept in the main repo, with all
# older ones going to the archive. Set it to 0, and there will be no archive
# repository, and no need to define the other archive_ values.
archive_older = 3
archive_url = "https://f-droid.org/archive"
archive_name = "My First F-Droid Archive Demo"
archive_icon = "fdroid-icon.png"
archive_description = """
The repository of older versions of applications from the main demo repository.
"""

# `fdroid update` will create a link to the current version of a given app.
# This provides a static path to the current APK.  To disable the creation of
# this link, uncomment this:
# make_current_version_link = False

# By default, the "current version" link will be based on the "Name" of the
# app from the metadata.  You can change it to use a different field from the
# metadata here:
# current_version_name_source = 'id'

# Optionally, override home directory for gpg
# gpghome = /home/fdroid/somewhere/else/.gnupg

# The ID of a GPG key for making detached signatures for apks. Optional.
# gpgkey = '1DBA2E89'

# The key (from the keystore defined below) to be used for signing the
# repository itself.  This is the same name you would give to keytool or
# jarsigner using -alias.  (Not needed in an unsigned repository).
repo_keyalias = "9c6c6a034fb6"

# Optionally, the public key for the key defined by repo_keyalias above can
# be specified here. There is no need to do this, as the public key can and
# will be retrieved from the keystore when needed. However, specifying it
# manually can allow some processing to take place without access to the
# keystore.
# repo_pubkey = "..."

# The keystore to use for release keys when building. This needs to be
# somewhere safe and secure, and backed up!  The best way to manage these
# sensitive keys is to use a "smartcard" (aka Hardware Security Module). To
# configure F-Droid to use a smartcard, set the keystore file using the keyword
# "NONE" (i.e. keystore = "NONE").  That makes Java find the keystore on the
# smartcard based on 'smartcardoptions' below.
keystore = "keystore.jks"

# You should not need to change these at all, unless you have a very
# customized setup for using smartcards in Java with keytool/jarsigner
# smartcardoptions = "-storetype PKCS11 -providerName SunPKCS11-OpenSC \
#    -providerClass sun.security.pkcs11.SunPKCS11 \
#    -providerArg opensc-fdroid.cfg"

# The password for the keystore (at least 6 characters).  If this password is
# different than the keypass below, it can be OK to store the password in this
# file for real use.  But in general, sensitive passwords should not be stored
# in text files!
keystorepass = "WJddze84qI9HxcooXODDqBaXU5Nj0SaxhaXYM4ae58U="

# The password for keys - the same is used for each auto-generated key as well
# as for the repository key.  You should not normally store this password in a
# file since it is a sensitive password.
keypass = "WJddze84qI9HxcooXODDqBaXU5Nj0SaxhaXYM4ae58U="

# The distinguished name used for all keys.
keydname = "CN=9c6c6a034fb6, OU=F-Droid"

# Use this to override the auto-generated key aliases with specific ones
# for particular applications. Normally, just leave it empty.
keyaliases = {}
keyaliases['com.example.app'] = 'example'
# You can also force an app to use the same key alias as another one, using
# the @ prefix.
keyaliases['com.example.another.plugin'] = '@com.example.another'


# The full path to the root of the repository.  It must be specified in
# rsync/ssh format for a remote host/path. This is used for syncing a locally
# generated repo to the server that is it hosted on.  It must end in the
# standard public repo name of "/fdroid", but can be in up to three levels of
# sub-directories (i.e. /var/www/packagerepos/fdroid).  You can include
# multiple servers to sync to by wrapping the whole thing in {} or [], and
# including the serverwebroot strings in a comma-separated list.
#
# serverwebroot = 'user@example:/var/www/fdroid'
# serverwebroot = {
#     'foo.com:/usr/share/nginx/www/fdroid',
#     'bar.info:/var/www/fdroid',
#     }


# optionally specific which identity file to use when using rsync over SSH
#
# identity_file = '~/.ssh/fdroid_id_rsa'


# If you are running the repo signing process on a completely offline machine,
# which provides the best security, then you can specify a folder to sync the
# repo to when running `fdroid server update`.  This is most likely going to
# be a USB thumb drive, SD Card, or some other kind of removable media.  Make
# sure it is mounted before running `fdroid server update`.  Using the
# standard folder called 'fdroid' as the specified folder is recommended, like
# with serverwebroot.
#
# local_copy_dir = '/media/MyUSBThumbDrive/fdroid'


# If you are using local_copy_dir on an offline build/signing server, once the
# thumb drive has been plugged into the online machine, it will need to be
# synced to the copy on the online machine.  To make that happen
# automatically, set sync_from_local_copy_dir to True:
#
# sync_from_local_copy_dir = True


# To upload the repo to an Amazon S3 bucket using `fdroid server update`.
# Warning, this deletes and recreates the whole fdroid/ directory each
# time. This is based on apache-libcloud, which supports basically all cloud
# storage services, so it should be easy to port the fdroid server tools to
# any of them.
#
awsbucket = 'qa-martus-sag'
awsaccesskeyid = 'AKIAIS53YP7S3XL7QZXA'
awssecretkey = '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'


# If you want to force 'fdroid server' to use a non-standard serverwebroot
#
# nonstandardwebroot = False


# Wiki details
wiki_protocol = "http"
wiki_server = "server"
wiki_path = "/wiki/"
wiki_user = "login"
wiki_password = "1234"

# Only set this to true when running a repository where you want to generate
# stats, and only then on the master build servers, not a development
# machine.
update_stats = False

# When used with stats, this is a list of IP addresses that are ignored for
# calculation purposes.
stats_ignore = []

# Server stats logs are retrieved from. Required when update_stats is True.
stats_server = "example.com"

# User stats logs are retrieved from. Required when update_stats is True.
stats_user = "bob"

# Use the following to push stats to a Carbon instance:
stats_to_carbon = False
carbon_host = '0.0.0.0'
carbon_port = 2003

# Set this to true to always use a build server. This saves specifying the
# --server option on dedicated secure build server hosts.
build_server_always = False

# Limit in number of characters that fields can take up
# Only the fields listed here are supported, defaults shown
char_limits = {
    'Summary': 50,
    'Description': 1500
}

sdk_path = "$ANDROID_HOME"

build_tools = "22.0.1"

keystore = "keystore.jks"

keystorepass = "WJddze84qI9HxcooXODDqBaXU5Nj0SaxhaXYM4ae58U="

keypass = "WJddze84qI9HxcooXODDqBaXU5Nj0SaxhaXYM4ae58U="

repo_keyalias = "9c6c6a034fb6"

keydname = "CN=9c6c6a034fb6, OU=F-Droid"
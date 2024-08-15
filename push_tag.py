# Taken from Akashic Tome with permission of vazkii
import os
import re
from jproperties import Properties


def main():
    build = Properties()
    with open('gradle.properties', 'rb') as f:
        build.load(f, "utf-8")

    mc_version, mcv_meta = build['mc_version']
    mod_version, v_meta = build['mod_version']

    print('MC Version:', mc_version)
    print('Version:', mod_version)

    changelog = '-m "Changelog:" '
    with open('changelog.txt', 'r') as f:
        content = f.read()

        content = content.replace('"', '\'');
        changelog = changelog + re.sub(r'(- .+)\n?', '-m "\g<1>" ', content)

    os.system('git tag -a release-{}-{} {}'.format(mc_version, mod_version, changelog))

    with open("gradle.properties", "wb") as f:
        build.store(f, encoding="utf-8")

    os.system('git commit -a -m build')
    os.system('git push origin 1.19.x release-{}-{}'.format(mc_version, mod_version))


if __name__ == '__main__':
    main()

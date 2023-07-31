"""
    System that detects if the bullet on the ui should be visible depending
    on how many bullets are left
"""

# make class bullet
class bullet:
    # Make bullet visible on default
    def __init__(self):
        self.visible = True

# Make all bullet objects
bullet1 = bullet();bullet2 = bullet();bullet3 = bullet();bullet4 = bullet();bullet5 = bullet()

# List of bullet objects
bullets = [bullet1, bullet2, bullet3, bullet4, bullet5]

# Ammo vars (ones being tested)
maxAmmo = len(bullets)
ammoLeft = 5

# Make bullets not visible depending on how much ammo was used
for x in range(maxAmmo-ammoLeft):
    bullets[x].visible = False

# Show results
for x in bullets:
    print(x.visible)
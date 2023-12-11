import json

import pandas
import matplotlib.pyplot as plt
import math
import random
import numpy

# --------------------------------------
# Initialisation du plot
# --------------------------------------
# z points, k centres, points construits autour des centres avec un décalage. On rajoute 0.2z points completement aleatoires
nb_points = 500
nb_centres = 3
nb_points_random = int(nb_points // 5)
offset = 2
points = []

fig, (ax1, ax2) = plt.subplots(1, 2)

centres = []

for ixe in range(nb_centres):
    coordonnees_point = (random.random() * 10, random.random() * 10)
    centres.append(coordonnees_point)
    points.append(coordonnees_point)

for ixe in range(nb_points):
    a = random.random() * math.pi * 2
    r = (random.random() * offset) ** 2
    x = math.cos(a) * r
    y = math.sin(a) * r
    centre_choisi = centres[random.randint(0, len(centres) - 1)]
    ax1.scatter(centre_choisi[0] + x, centre_choisi[1] + y, color='r')
    points.append((centre_choisi[0] + x, centre_choisi[1] + y))

for ixe in range(nb_points_random):
    x = random.random() * (10 + ((offset ** 2) * 2)) - offset ** 2
    y = random.random() * (10 + ((offset ** 2) * 2)) - offset ** 2
    ax1.scatter(x=x,
                y=y, color='r')
    points.append((x, y))

for centre_actuel in centres:
    ax1.scatter(centre_actuel[0], centre_actuel[1], color='y')
ax1.set_aspect(1)
# print(json.dumps(points, indent=4))

# --------------------------------------
# Algorithme des k-medoides
# --------------------------------------
config_cost = 0  # le cout de configuration est la somme des distances de chaque point avec son medoide
supposed_cost = 0
medoides = []
# Selection des medoides
for ixe in range(nb_centres):
    medoides.append(points[random.randint(0, len(points) - 1)])
# Calcul du cout de config
# On calcule la distance de manière à associer chaque objet de données à son plus proche médoïde
distances_medoides = {}
# print(*points, sep='\n')
for coordonnees_point in points:
    distances_point_to_centres = []
    medoide = []
    for centre_actuel in medoides:
        distances_point_to_centres.append(math.dist([coordonnees_point[0], coordonnees_point[1]], centre_actuel))
        medoide.append(centre_actuel)

    min_distance = min(distances_point_to_centres)
    if not distances_medoides.get(str(medoide[distances_point_to_centres.index(min_distance)])):
        distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])] = [[[coordonnees_point[0], coordonnees_point[1]], min_distance]]
    else:
        distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])].append([[coordonnees_point[0], coordonnees_point[1]], min_distance])

couleur = ['r', 'g', 'b', 'c', 'm', 'y']
cles_dict = list(distances_medoides.keys())

for x in range(len(distances_medoides)):
    couleur_actuelle = couleur[x]
    for element in distances_medoides[cles_dict[x]]:
        ax2.scatter(element[0][0], element[0][1], color=couleur_actuelle, marker="x")


# affichage centre random

for x in medoides:
    ax2.scatter(x[0], x[1], color='#000', marker="+")




ax2.set_aspect(1)

for element in distances_medoides.values():
    for co_point_et_distance in element:
        config_cost += co_point_et_distance[1]
print(f'Cout de config:{config_cost}')
# (SWAPPING) TANT QUE LE COUT DE LA CONFIGURATION DIMINUE
'''while supposed_cost < config_cost :
    for elt in medoides:
        for point in points:
            if point == elt:
                pass
            else:'''

#   POUR CHAQUE MEDOIDE M ET POUR CHAQUE POINT NON MEDOIDE O
#       CONSIDERER L ECHANGE DE M ET O ET CALCULER LE COUT D ECHANGE
#       SI LE COUT EST LE MEILLEUR ACTUELLEMENT
#           GARDER EN MEMOIRE LA COMBINAISON M ET O
#   SI L ECHANGE DE M_MEILLEUR ET O_MEILLEUR REDUIT LA FONCTION DE COUT
#       FAIRE L ECHANGE
#   SINON
#       ARRETER


# plt.legend()
plt.show()
plt.close()

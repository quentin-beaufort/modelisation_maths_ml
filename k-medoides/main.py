import matplotlib.pyplot as plt
import math
import random
import copy

# --------------------------------------
# Initialisation du plot
# --------------------------------------
# paramètres inititaux
nb_points = 400
nb_centres = 3
nb_points_random = int(nb_points // 5)  # On rajoute 1/5 des points totaux en points completement aleatoires
offset = 2  # On définit un décalage des points par rapport à leur centre
subplots_counter = 2
subplots_row_pointer = 1
subplots_col_pointer = False

fig, axs = plt.subplots(3, 2)

ax1 = axs[0, 0]
ax2 = axs[0, 1]

points = []
centres = []

# On choisit les medoides initiaux
for ixe in range(nb_centres):
    coordonnees_point = (random.random() * 10, random.random() * 10)
    centres.append(coordonnees_point)
    points.append(coordonnees_point)

# On place les points autour des centres en calculant un décalage circulaire
for ixe in range(nb_points):
    a = random.random() * math.pi * 2
    r = (random.random() * offset) ** 2
    x = math.cos(a) * r
    y = math.sin(a) * r
    centre_choisi = centres[random.randint(0, len(centres) - 1)]
    ax1.scatter(centre_choisi[0] + x, centre_choisi[1] + y, color='r')
    points.append((centre_choisi[0] + x, centre_choisi[1] + y))

# On place quelques points complètement aléatoires
for ixe in range(nb_points_random):
    x = random.random() * (10 + ((offset ** 2) * 2)) - offset ** 2
    y = random.random() * (10 + ((offset ** 2) * 2)) - offset ** 2
    ax1.scatter(x=x, y=y, color='r')
    points.append((x, y))

# On place les centres
for centre_actuel in centres:
    ax1.scatter(centre_actuel[0], centre_actuel[1], color='y')

# Définition d'un ratio d'aspect pour que le diagramme soit facilement lisible
ax1.set_title('Build initial')
ax1.set_aspect(1)

# --------------------------------------
# Algorithme des k-medoides
# --------------------------------------
config_cost = 0  # le cout de configuration est la somme des distances de chaque point avec son medoide
supposed_cost = 0  # le cout supposé est le coût de configuration possible si on effectuait un échange de médoïde
medoides = []

# Selection des medoides
for ixe in range(nb_centres):
    medoides.append(points[random.randint(0, len(points) - 1)])

# On calcule la distance de manière à associer chaque point à son plus proche médoïde
distances_medoides = {}  # Ce dictionnaire est de la forme clé = coordonnées d'un médoide et valeur = [[x, y], distance avec le medoide]
for coordonnees_point in points:
    distances_point_to_centres = []
    medoide = []
    for centre_actuel in medoides:
        distances_point_to_centres.append(math.dist([coordonnees_point[0], coordonnees_point[1]], centre_actuel))
        medoide.append(centre_actuel)

    min_distance = min(distances_point_to_centres)
    if not distances_medoides.get(str(medoide[distances_point_to_centres.index(min_distance)])):
        distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])] = [
            [[coordonnees_point[0], coordonnees_point[1]], min_distance]]
    else:
        distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])].append(
            [[coordonnees_point[0], coordonnees_point[1]], min_distance])

# À ce moment-là, on a un dictionnaire avec les coordonnées de chaque point associé à leur médoide
# On peut donc les placer et les colorier uniformément selon leur médoïde
couleur = ['r', 'g', 'b', 'c', 'm', 'y']
cles_dict = list(distances_medoides.keys())

for x in range(len(distances_medoides)):
    couleur_actuelle = couleur[x]
    for element in distances_medoides[cles_dict[x]]:
        ax2.scatter(element[0][0], element[0][1], color=couleur_actuelle, marker="x")

# On place les médoïdes en forme de croix pour bien les repérer
for x in medoides:
    ax2.scatter(x[0], x[1], color='#000', marker="+")

# Définition d'un ratio d'aspect pour que le diagramme soit facilement lisible
ax2.set_title('Medoides initiaux')
ax2.set_aspect(1)

# Calcul du cout de config pour la suite
for element in distances_medoides.values():
    for co_point_et_distance in element:
        config_cost += co_point_et_distance[1]

print(f'Cout de config initial:{config_cost}')

# On va maintenant essayer de réduire le cout de configuration en échangeant les médoïdes
# On va donc parcourir les médoïdes et pour chacun d'entre eux, on va parcourir les points
for x in range(len(medoides)-1, -1, -1):
    for o in points:
        # les medoides sont dans le tableau des points donc on ne va pas calculer le cout d'echange du medoide avec lui meme
        if medoides[x] == o:
            pass
        else:
            # copie profonde de medoides puisque l'on va modifier la liste
            medoides_copie = copy.deepcopy(medoides)
            # on supprime le medoide actuel
            medoides_copie.pop(x)
            # on ajoute le nouveau medoide
            medoides_copie.append(o)
            # calcul distances
            distances_medoides = {}  # Ce dictionnaire est de la forme clé = coordonnées d'un médoide et valeur = [[x, y], distance avec le medoide]
            for coordonnees_point in points:
                distances_point_to_centres = []
                medoide = []
                for centre_actuel in medoides_copie:
                    distances_point_to_centres.append(
                        math.dist([coordonnees_point[0], coordonnees_point[1]], centre_actuel))
                    medoide.append(centre_actuel)

                min_distance = min(distances_point_to_centres)
                if not distances_medoides.get(str(medoide[distances_point_to_centres.index(min_distance)])):
                    distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])] = [
                        [[coordonnees_point[0], coordonnees_point[1]], min_distance]]
                else:
                    distances_medoides[str(medoide[distances_point_to_centres.index(min_distance)])].append(
                        [[coordonnees_point[0], coordonnees_point[1]], min_distance])

            # calcul cout supposé
            for element in distances_medoides.values():
                for co_point_et_distance in element:
                    supposed_cost += co_point_et_distance[1]

            if supposed_cost < config_cost:
                # On supprime l'ancien médoide dans la liste itéré et on ajoute le nouveau
                medoides.pop(x)
                medoides.append(o)

                couleur = ['r', 'g', 'b', 'c', 'm', 'y']
                cles_dict = list(distances_medoides.keys())

                # Selection du subplot
                if subplots_col_pointer:
                    ax_current = axs[subplots_row_pointer, 1]
                    subplots_col_pointer = False
                    subplots_row_pointer += 1
                    subplots_counter += 1
                else:
                    ax_current = axs[subplots_row_pointer, 0]
                    subplots_col_pointer = True
                    subplots_counter += 1

                # On place les points selon leur médoïde
                for x in range(len(distances_medoides)):
                    couleur_actuelle = couleur[x]
                    for element in distances_medoides[cles_dict[x]]:
                        ax_current.scatter(element[0][0], element[0][1], color=couleur_actuelle, marker="x")

                # On place les médoïdes en forme de croix pour bien les repérer
                for z in medoides_copie:
                    ax_current.scatter(z[0], z[1], color='#000', marker="+")

                ax_current.set_title(f'Swap {subplots_counter-2}')
                ax_current.set_aspect(1)
                print(f'Cout de config supposé:{supposed_cost}')
                config_cost = supposed_cost  # On met à jour le cout de configuration
                supposed_cost = 0  # On remet à 0 le cout supposé
            else:
                # Le cout de configuration est plus grand que le cout supposé donc on arrête
                break

# On supprime les subplots inutiles
for j in range(subplots_counter, len(axs.flatten())):
    fig.delaxes(axs.flatten()[j])

plt.tight_layout()
plt.show()
plt.close()
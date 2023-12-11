import pandas
import matplotlib.pyplot as plt
import math
import random

fig, ax = plt.subplots()

iris = pandas.read_csv("iris.csv")
x = iris.loc[:, "petal_length"]
y = iris.loc[:, "petal_width"]
lab = iris.loc[:, "species"]

# valeurs de l'algo et intrus
k = 5
intrus_x = random.random() * 7 + .1
intrus_y = random.random() * 2.5 + .01

distance_points = []
# parcours liste
for i in range(len(x)):
   # calcul distance
   dist = math.dist([intrus_x, intrus_y], [x[i], y[i]])
   distance_points.append((dist, [x[i], y[i], lab[i]]))

# liste de k elts
distance_points = sorted(distance_points)[:k]
# calcul espece majoritaire
majorite_types = {}
for ixe in distance_points:
   if ixe[1][2] not in majorite_types:
       majorite_types[ixe[1][2]] = 1
   else:
       majorite_types[ixe[1][2]] += 1
intrus_type = max(majorite_types, key=majorite_types.get)

print(intrus_type)

ax.scatter(x[lab == 'setosa'], y[lab == 'setosa'], color='g', label='setosa')
ax.scatter(x[lab == 'virginica'], y[lab == 'virginica'], color='r', label='virginica')
ax.scatter(x[lab == 'versicolor'], y[lab == 'versicolor'], color='b', label='versicolor')
ax.scatter(intrus_x, intrus_y, color='black', label=f'résolu:{intrus_type}')
plt.legend()
ax.set_aspect(1)
plt.show()
plt.close()
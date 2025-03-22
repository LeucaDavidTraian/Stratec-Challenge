#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <string>
#include <regex>

using namespace std;
// Easy way to define the data in order for it to be easy to use later on
struct Planet {
    string name;
    double mass;
    double radius;
};

double calculateEscapeVelocity(double mass, double radius) {
    const double G = 6.67e-11; // Gravitational constant (m^3 kg^-1 s^-2)
    double radius_meters = radius * 1000; // Convert km to m
    return sqrt((2 * G * mass) / radius_meters);
}

vector<Planet> readPlanetaryData(const string& filePath) {
    vector<Planet> planets;
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: File not found." << endl;
        return planets;
    }

    string line;
    // Updated regex to support variations like "6 * 10^24 kg"
    regex pattern(R"(([^:]+):\s*diameter\s*=\s*([0-9]+)\s*km,\s*mass\s*=\s*([0-9]+(?:\s*\*\s*10\^?[0-9]+|(?:\.[0-9]+)?))\s*(Earths|kg)?)");
    smatch matches;
    const double EARTH_MASS = 5.972e24; // Earth mass in kg

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            string name = matches[1];
            double diameter = stod(matches[2]);
            string massStr = matches[3];
            double mass;

            // Handle scientific notation manually
            if (massStr.find('*') != string::npos) {
                size_t pos = massStr.find('*');
                double base = stod(massStr.substr(0, pos));
                size_t expPos = massStr.find('^', pos);
                int exponent = stoi(massStr.substr(expPos + 1));
                mass = base * pow(10, exponent);
            } else {
                mass = stod(massStr);
            }

            if (matches[4] == "Earths") {
                mass *= EARTH_MASS;
            }

            double radius = diameter / 2; // Convert diameter to radius
            planets.push_back({name, mass, radius});
        } else {
            cerr << "Warning: Skipping malformed line -> " << line << endl;
        }
    }
    return planets;
}

void saveResultsToFile(const vector<Planet>& planets, const string& outputFile) {
    ofstream file(outputFile);
    if (!file) {
        cerr << "Error: Unable to create output file." << endl;
        return;
    }
    for (const auto& planet : planets) {
        double velocity = calculateEscapeVelocity(planet.mass, planet.radius);
        file << planet.name << ": " << velocity / 1000 << " km/s\n";
    }
    cout << "Results saved to " << outputFile << endl;
}

int main() {
    string filePath;
    cout << "Enter the path to the planetary data file: ";
    cin >> filePath;

    vector<Planet> planets = readPlanetaryData(filePath); // reading all of the data into the struct
    if (planets.empty()) {
        return 1;
    }

    cout << "\nPlanetary Escape Velocities:" << endl;
    for (const auto& planet : planets) {
        double velocity = calculateEscapeVelocity(planet.mass, planet.radius);
        cout << planet.name << ": " << velocity / 1000 << " km/s" << endl;    //output in SI
    } 

    char saveOutput;
    cout << "Do you want to save the results to a file? (y/n): ";
     cin >> saveOutput;
    
    if (saveOutput == 'y' || saveOutput == 'Y') {
        string outputFile = "Escape_Velocities.txt"; // Default file name
        char customName;
        cout << "Do you want to specify a custom file name? (y/n): ";
        cin >> customName;

        if (customName == 'y' || customName == 'Y') {
            cout << "Enter the output file name: ";
            cin >> outputFile;
        }

        saveResultsToFile(planets, outputFile);
    }

    return 0;
}
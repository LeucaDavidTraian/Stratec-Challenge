#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <cmath>
#include <string>
#include <regex>

using namespace std;

struct Planet {
    string name;
    double mass;
    double radius;
};

const double G = 6.67e-11;
const double EARTH_MASS = 5.972e24;

double ENGINE_ACCELERATION;
int ENGINE_COUNT;
double TOTAL_ACCELERATION;

double calculateEscapeVelocity(double mass, double radius) {   // same as before
    double radius_meters = radius * 1000;
    return sqrt((2 * G * mass) / radius_meters);
}

void computeRocketPhysics(double escapeVelocity, double planetRadius, double &time, double &distance) {
    time = escapeVelocity / TOTAL_ACCELERATION;
    distance = 0.5 * TOTAL_ACCELERATION * time * time;
}

vector<Planet> readPlanetaryData(const string& filePath) {
    vector<Planet> planets;
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: Could not open planetary data file." << endl;
        return planets;
    }

    string line;
    regex pattern(R"(([^:]+):\s*diameter\s*=\s*([0-9]+)\s*km,\s*mass\s*=\s*([0-9\.eE\^\*\s]+)\s*(Earths|kg))");   // adapted to read both kinds of format
    smatch matches;

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            string name = matches[1];
            double diameter = stod(matches[2]);
            string massStr = matches[3];
            double mass;
            
            massStr = regex_replace(massStr, regex("\\s*\\*\\s*10\\^"), "e");   // so both 10^24 and e24
            massStr = regex_replace(massStr, regex("\\s*\\^\\s*"), "e");
            
            if (matches[4] == "kg") {
                mass = stod(massStr);
            } else {
                mass = stod(massStr) * EARTH_MASS;
            }

            double radius = diameter / 2;
            planets.push_back({name, mass, radius});
        } else {
            cerr << "Warning: Skipping malformed line -> " << line << endl;
        }
    }
    return planets;
}

void readRocketData(const string& filePath) {
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: Could not open rocket data file." << endl;
        exit(1);
    }

    string line;
    regex pattern(R"((Number of rocket engines):\s*([0-9]+)|(Acceleration per engine):\s*([0-9\.]+)\s*m/s\^2)");
    smatch matches;

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            if (matches[1] == "Number of rocket engines") {
                ENGINE_COUNT = stoi(matches[2]);
            } else if (matches[3] == "Acceleration per engine") {
                ENGINE_ACCELERATION = stod(matches[4]);
            }
        }
    }
    TOTAL_ACCELERATION = ENGINE_COUNT * ENGINE_ACCELERATION;
}

void saveResultsToFile(const vector<Planet>& planets, const string& outputFile) {   // complete and fancy output
    ofstream file(outputFile);
    if (!file) {
        cerr << "Error: Unable to create output file." << endl;
        return;
    }

    file << "Rocket Escape Velocity Calculations\n";
    file << "----------------------------------\n";

    for (const auto& planet : planets) {
        double escapeVelocity = calculateEscapeVelocity(planet.mass, planet.radius);
        double time, distance;
        computeRocketPhysics(escapeVelocity, planet.radius, time, distance);

        double totalDistanceFromCenter = planet.radius * 1000 + distance;

        file << planet.name << ":\n";
        file << "  Escape Velocity: " << escapeVelocity / 1000 << " km/s\n";
        file << "  Time to Escape: " << time << " seconds\n";
        file << "  Distance Traveled: " << distance / 1000 << " km\n";
        file << "  Total Distance from Center: " << totalDistanceFromCenter / 1000 << " km\n";
        file << "----------------------------------\n";
    }

    cout << "Results saved to " << outputFile << endl;
}

int main() {
    string planetFilePath, rocketFilePath;
    cout << "Enter file path containing planetary data: ";
    cin >> planetFilePath;
    cout << "Enter file path containing rocket data: ";
    cin >> rocketFilePath;
    
    readRocketData(rocketFilePath);
    vector<Planet> planets = readPlanetaryData(planetFilePath);
    if (planets.empty()) {
        return 1;
    }

    cout << "\nRocket Escape Velocity Calculations:\n";
    for (const auto& planet : planets) {
        double escapeVelocity = calculateEscapeVelocity(planet.mass, planet.radius);
        double time, distance;
        computeRocketPhysics(escapeVelocity, planet.radius, time, distance);

        double totalDistanceFromCenter = planet.radius * 1000 + distance;

        cout << planet.name << ":\n";
        cout << "  Escape Velocity: " << escapeVelocity / 1000 << " km/s\n";
        cout << "  Time to Escape: " << time << " seconds\n";
        cout << "  Distance Traveled: " << distance / 1000 << " km\n";
        cout << "  Total Distance from Center: " << totalDistanceFromCenter / 1000 << " km\n";
        cout << "----------------------------------\n";
    }

    char saveOutput;
    cout << "Do you want to save the results to a file? (y/n): ";  
    cin >> saveOutput;

    if (saveOutput == 'y' || saveOutput == 'Y') {
        string outputFile = "Rocket_Escape_Data.txt";
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
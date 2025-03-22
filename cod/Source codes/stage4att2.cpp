#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <string>
#include <regex>
#include <algorithm>

using namespace std;

struct Planet {
    string name;
    double orbital_radius;
    double orbital_period;
};

const double AU_TO_KM = 149597870.7;
const double PI = 3.141592653589793;

vector<Planet> readOrbitalData(const string& filePath) {
    vector<Planet> planets;
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: File not found." << endl;
        return planets;
    }

    string line;
    regex pattern(R"(([^:]+):\s*period\s*=\s*([0-9]+)\s*days,\s*orbital radius\s*=\s*([0-9\.]+)\s*AU)");
    smatch matches;

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            string name = matches[1];
            double period = stod(matches[2]);
            double orbital_radius = stod(matches[3]) * AU_TO_KM;
            planets.push_back({name, orbital_radius, period});
        }
    }
    return planets;
}

int main() {
    string orbitFilePath;
    cout << "Enter the path to the planetary orbit data file: ";
    cin >> orbitFilePath;

    vector<Planet> planets = readOrbitalData(orbitFilePath);
    if (planets.empty()) {
        cerr << "Error: No planets were loaded from the data file." << endl;
        return 1;
    }

    double input_days;
    cout << "Enter the number of days to simulate planetary positions: ";
    cin >> input_days;

    cout << "Planetary Positions after " << input_days << " days:" << endl;
    for (const auto& planet : planets) {
        double angular_position = fmod((360.0 / planet.orbital_period) * input_days, 360.0);
        cout << planet.name << ": " << angular_position << " degrees" << endl;
    }

    char saveToFile;
    cout << "Do you want to save the results to a file? (y/n): ";  //user input
    cin >> saveToFile;

    if (saveToFile == 'y' || saveToFile == 'Y') {
        string outputFileName;
        cout << "Enter the output file name: ";
        cin >> outputFileName;
        ofstream outputFile(outputFileName);
        if (!outputFile) {
            cerr << "Error: Unable to create output file." << endl;
            return 1;
        }
        outputFile << "Planetary Positions after " << input_days << " days:" << endl;
        for (const auto& planet : planets) {
            double angular_position = fmod((360.0 / planet.orbital_period) * input_days, 360.0);
            outputFile << planet.name << ": " << angular_position << " degrees" << endl;
        }
        outputFile.close();
        cout << "Results saved to " << outputFileName << endl;
    }

    return 0;
}
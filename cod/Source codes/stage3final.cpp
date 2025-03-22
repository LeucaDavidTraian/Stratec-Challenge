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
    double radius; // in km
    double mass; // in kg
    double orbital_radius; // in AU
    double escape_velocity; // in km/s
};

struct RocketData {
    int num_engines;
    double acceleration_per_engine; // in m/s^2
};

const double EARTH_MASS = 5.972e24; // kg
const double G = 6.67430e-11; // m^3 kg^-1 s^-2
const double AU_TO_KM = 149597870.7; // 1 AU in km

vector<Planet> readPlanetaryData(const string& filePath) {
    vector<Planet> planets;
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: File not found." << endl;
        return planets;
    }

    string line;
    regex pattern(R"(([^:]+):\s*diameter\s*=\s*([0-9\.]+)\s*km,\s*mass\s*=\s*([0-9\.e\^\*\s]+)\s*(Earths|kg)?)");    // again the need to read both in kilometers or in number of earths
    smatch matches;

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            string name = matches[1];
            double diameter = stod(matches[2]);
            string massStr = matches[3];
            double mass;

            if (matches[4] == "Earths") {
                mass = stod(massStr) * EARTH_MASS;
            } else {
                massStr = regex_replace(massStr, regex("\\*\\s*10\\^"), "e");
                mass = stod(massStr);
            }

            double radius = diameter / 2.0;
            planets.push_back({name, radius, mass, 0.0, 0.0});
        } else {
            cerr << "Warning: Skipping malformed line -> " << line << endl;
        }
    }
    return planets;
}

vector<Planet> readOrbitalData(const string& filePath, vector<Planet>& planets) {
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: File not found." << endl;
        return planets;
    }

    string line;
    regex pattern(R"(([^:]+):\s*period\s*=\s*[0-9]+\s*days,\s*orbital radius\s*=\s*([0-9\.]+)\s*AU)");  
    smatch matches;

    while (getline(file, line)) {
        if (regex_search(line, matches, pattern)) {
            string name = matches[1];
            double orbital_radius = stod(matches[2]);
            for (auto& planet : planets) {
                if (planet.name == name) {
                    planet.orbital_radius = orbital_radius;
                    break;
                }
            }
        }
    }
    return planets;
}

RocketData readRocketData(const string& filePath) {
    RocketData rocketData = {0, 0.0};
    ifstream file(filePath);
    if (!file) {
        cerr << "Error: Rocket data file not found." << endl;
        return rocketData;
    }

    string line;
    while (getline(file, line)) {
        if (line.find("Number of rocket engines:") != string::npos) {
            rocketData.num_engines = stoi(line.substr(line.find(":") + 1));
        } else if (line.find("Acceleration per engine:") != string::npos) {
            rocketData.acceleration_per_engine = stod(line.substr(line.find(":") + 1));
        }
    }
    return rocketData;
}

double calculateEscapeVelocity(double mass, double radius) {
    return sqrt((2 * G * mass) / (radius * 1000)); // Convert radius to meters
}

void calculateTravelParameters(const Planet& start, const Planet& destination, const RocketData& rocketData, ofstream& outputFile) {
    // Debug: Print planet data
    cout << "Start Planet: " << start.name << ", Radius: " << start.radius << " km, Escape Velocity: " << start.escape_velocity << " km/s" << endl;
    cout << "Destination Planet: " << destination.name << ", Radius: " << destination.radius << " km, Escape Velocity: " << destination.escape_velocity << " km/s" << endl;

    // Distance between the two planets (center to center) in km
    double distance = fabs(destination.orbital_radius - start.orbital_radius) * AU_TO_KM;
    cout << "Distance between centers: " << distance << " km" << endl;

    // Subtract the radii of the starting and destination planets to account for surface-to-surface travel
    distance -= (start.radius + destination.radius);
    cout << "Surface-to-surface distance: " << distance << " km" << endl;

    // Cruising velocity is the maximum escape velocity of the two planets
    double cruising_velocity = max(start.escape_velocity, destination.escape_velocity);
    cout << "Cruising velocity: " << cruising_velocity << " km/s" << endl;

    // Total acceleration of the rocket
    double total_acceleration = rocketData.num_engines * rocketData.acceleration_per_engine; // in m/s²
    total_acceleration /= 1000; // Convert to km/s²
    cout << "Total acceleration: " << total_acceleration << " km/s²" << endl;

    // Time to reach cruising velocity
    double acceleration_time = cruising_velocity / total_acceleration;
    cout << "Acceleration time: " << acceleration_time << " seconds" << endl;

    // Distance covered during acceleration
    double acceleration_distance = 0.5 * total_acceleration * pow(acceleration_time, 2);
    cout << "Acceleration distance: " << acceleration_distance << " km" << endl;

    // Distance from the starting planet's surface when cruising velocity is reached
    double start_distance = acceleration_distance;

    // Distance from the destination planet's surface when deceleration starts
    double destination_distance = acceleration_distance;

    // Total cruising distance
    double cruising_distance = distance - (start_distance + destination_distance);
    cout << "Cruising distance: " << cruising_distance << " km" << endl;

    // Time spent cruising
    double cruising_time = cruising_distance / cruising_velocity;
    cout << "Cruising time: " << cruising_time << " seconds" << endl;

    // Total travel time
    double total_time = acceleration_time + cruising_time + acceleration_time;

    // Convert total time to days, hours, minutes, and seconds
    int days = total_time / (24 * 3600);
    int hours = (total_time - (days * 24 * 3600)) / 3600;
    int minutes = (total_time - (days * 24 * 3600) - (hours * 3600)) / 60;
    int seconds = total_time - (days * 24 * 3600) - (hours * 3600) - (minutes * 60);

    // Output the results to the file
    outputFile << "\nTravel Parameters:" << endl;
    outputFile << "1. Time to reach cruising velocity: " << acceleration_time << " seconds" << endl;
    outputFile << "2. Distance from starting planet's surface when cruising velocity is reached: " << start_distance << " km" << endl;
    outputFile << "3. Cruising time: " << cruising_time << " seconds" << endl;
    outputFile << "4. Distance from destination planet's surface when deceleration starts: " << destination_distance << " km" << endl;
    outputFile << "5. Time to decelerate to zero: " << acceleration_time << " seconds" << endl;
    outputFile << "6. Total travel time: " << total_time << " seconds (" << days << " days, " << hours << " hours, " << minutes << " minutes, " << seconds << " seconds)" << endl;
}

int main() {
    string massFilePath, orbitFilePath, rocketDataFilePath;     // all 3 input files in a row
    cout << "Enter the path to the planetary mass data file: ";
    cin >> massFilePath;
    cout << "Enter the path to the planetary orbit data file: ";
    cin >> orbitFilePath;
    cout << "Enter the path to the rocket data file: ";
    cin >> rocketDataFilePath;

    vector<Planet> planets = readPlanetaryData(massFilePath);
    if (planets.empty()) {
        cerr << "Error: No planets were loaded from the data file." << endl;
        return 1;
    }

    planets = readOrbitalData(orbitFilePath, planets);

    RocketData rocketData = readRocketData(rocketDataFilePath);
    if (rocketData.num_engines == 0 || rocketData.acceleration_per_engine == 0.0) {
        cerr << "Error: Invalid rocket data." << endl;
        return 1;
    }

    // Calculate escape velocity for each planet
    for (auto& planet : planets) {
        planet.escape_velocity = calculateEscapeVelocity(planet.mass, planet.radius) / 1000; // Convert to km/s
    }

    cout << "Available planets:" << endl;
    for (const auto& planet : planets) {
        cout << " - " << planet.name << endl;
    }

    string startPlanet, destinationPlanet;
    cout << "Enter the starting planet: ";
    cin >> startPlanet;
    cout << "Enter the destination planet: ";
    cin >> destinationPlanet;

    transform(startPlanet.begin(), startPlanet.end(), startPlanet.begin(), ::tolower);
    transform(destinationPlanet.begin(), destinationPlanet.end(), destinationPlanet.begin(), ::tolower);

    Planet *start = nullptr, *destination = nullptr;
    for (auto &planet : planets) {
        string lowerName = planet.name;
        transform(lowerName.begin(), lowerName.end(), lowerName.begin(), ::tolower);
        if (lowerName == startPlanet) start = &planet;
        if (lowerName == destinationPlanet) destination = &planet;
    }

    if (!start || !destination) {
        cerr << "Error: Invalid planet names entered." << endl;
        return 1;
    }

    // Ask the user for the output file name
    string outputFileName;
    cout << "Enter the name of the output file: ";
    cin >> outputFileName;

    // Open the output file
    ofstream outputFile(outputFileName);
    if (!outputFile) {
        cerr << "Error: Unable to create output file." << endl;
        return 1;
    }

    // Calculate and write travel parameters to the output file
    calculateTravelParameters(*start, *destination, rocketData, outputFile);

    // Close the output file
    outputFile.close();

    cout << "Results saved to " << outputFileName << endl;

    return 0;
}
import { Route, Routes } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import OrchidDetail from "./components/OrchidDetail";
import OrchidsMenu from "./components/OrchidsMenu";
import NavBar from "./components/NavBar";
import Contact from "./components/Contact";
import About from "./components/About";
import OriginalOrchids from "./components/OriginalOrchids";
import Dashboard from "./dashboard/dashboard";

function App() {
	return (
		<div className="App">
			{/* <NavBar /> */}

			<Routes>
				<Route path="/" element={<OrchidsMenu />} />
				<Route path="/OrchidDetail/:id" element={<OrchidDetail />} />
				<Route path="/About" element={<About />} />
				<Route path="/Original" element={<OriginalOrchids />} />
				<Route path="/Contact" element={<Contact />} />
                <Route path="/dashboard" element={<Dashboard />} />				
			</Routes>
		</div>
	);
}

export default App;

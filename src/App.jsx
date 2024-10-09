import { Route, Routes } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import NavBar from "./components/NavBar";
import Guidelines from "./pages/Guidelines";
import About from "./pages/About";
import Login from "./pages/Login";
import Home from "./pages/Home";
import KoiPage from "./pages/KoiPage";
import KoiDetail from "./pages/KoiDetail";
import FarmPage from "./pages/FarmPage";
import FarmDetail from "./pages/FarmDetail";
import TourPage from "./pages/TourPage";
import TourDetail from "./pages/TourDetail";
import Register from "./pages/Register";
import AdminPage from "./pages/AdminPage";
import AdminStuff from "./components/AdminStuff";

function App() {
	return (
		<div className="App">
			<Routes>
				<Route path="/" element={<Home />} />
				<Route path="/About" element={<About />} />
				<Route path="/Guidelines" element={<Guidelines />} />
				<Route path="/KoiList" element={<KoiPage />} />
				<Route path="/KoiDetail/:id" element={<KoiDetail />} />
				<Route path="/FarmList" element={<FarmPage />} />
				<Route path="/FarmDetail/:id" element={<FarmDetail />} />
				<Route path="/TourList" element={<TourPage />} />
				<Route path="/TourDetail/:id" element={<TourDetail />} />
				<Route path="/Login" element={<Login />} />
				<Route path="/Register" element={<Register />} />

				<Route path="/Admin" element={<AdminPage cmp={AdminStuff} />} />
			</Routes>
		</div>
	);
}

export default App;

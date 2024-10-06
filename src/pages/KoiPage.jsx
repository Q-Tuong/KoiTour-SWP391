import React, { useEffect, useState } from "react";
import { Button, Container } from "react-bootstrap";
import Koi from "../components/Koi";
import { KoiList } from "../data/KoiList";
import NavBar from "../components/NavBar";
import AddKoi from "../components/AddKoi";

function KoiPage() {
	const [koilist, setKoilist] = useState([]);
	const [isOpen, setIsOpen] = useState(false);

	const api = "https://66fe49e22b9aac9c997b30ef.mockapi.io/Koi";

	async function fetchKoi() {
		try {
			const res = await fetch(api);
			if (!res.ok) {
				throw new Error(`Error: ${res.status}`);
			}
			const data = await res.json();
			console.log(data);
			setKoilist(data);
		} catch (error) {
			console.error("Failed to fetch koi:", error);
		}
	}

	useEffect(() => {
		fetchKoi();
	}, []);

	return (
		<>
			<NavBar />
			<Container>
				<h2>Koi to buy</h2>
				<Koi koilist={koilist} />
				<Button onClick={() => setIsOpen(true)}>Add</Button>
			</Container>
			{isOpen && <AddKoi setIsOpen={setIsOpen} open={isOpen} />}
		</>
	);
}

export default KoiPage;

import React, { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";

function AddKoi({ setIsOpen, open }) {
	const [category, setCategory] = useState("");
	const [description, setDescription] = useState("");
	const [color, setColor] = useState("");
	const [weight, setWeight] = useState("");
	const [size, setSize] = useState("");
	const [origin, setOrigin] = useState("");
	const [image, setImage] = useState("");

	const api = ""; //API Link goes here

	const handleClose = () => {
		setIsOpen(false);
	};

	async function add() {
		console.warn(category, description, color, weight, size, origin, image);
		const formData = new FormData();
		formData.append("category", category);
		formData.append("description", description);
		formData.append("color", color);
		formData.append("weight", weight);
		formData.append("size", size);
		formData.append("origin", origin);
		formData.append("image", image);
		let result = await fetch(api, {
			method: "POST",
			body: formData,
		});
		alert(result);
	}

	return (
		<>
			<Modal size="xl" centered show={open} onHide={handleClose}>
				<Modal.Header closeButton>
					<Modal.Title>Add new koi</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
						<Form.Group className="mb-3" controlId="formGroupCategory">
							<Form.Label>Category</Form.Label>
							<Form.Control type="text" placeholder="Category" onChange={(e) => setCategory(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupDesc">
							<Form.Label>Description</Form.Label>
							<Form.Control type="text" placeholder="Description" onChange={(e) => setDescription(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupColor">
							<Form.Label>Color</Form.Label>
							<Form.Control type="text" placeholder="Color" onChange={(e) => setColor(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupWeight">
							<Form.Label>Weight</Form.Label>
							<Form.Control type="text" placeholder="Weight" onChange={(e) => setWeight(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupSize">
							<Form.Label>Size</Form.Label>
							<Form.Control type="text" placeholder="Size" onChange={(e) => setSize(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupOrigin">
							<Form.Label>Origin</Form.Label>
							<Form.Control type="text" placeholder="Origin" onChange={(e) => setOrigin(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupImage">
							<Form.Label>Image</Form.Label>
							<Form.Control type="file" placeholder="ImageURL" onChange={(e) => setImage(e.target.files[0])} />
						</Form.Group>
					</Form>
				</Modal.Body>
				<Modal.Footer>
					<Button onClick={add}>Add</Button>
					{/* <Button type="reset">Reset</Button> */}
					<Button onClick={handleClose}>Cancel</Button>
				</Modal.Footer>
			</Modal>
		</>
	);
}

export default AddKoi;

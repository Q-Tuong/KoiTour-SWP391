import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function AdminPage(props) {
	let Cmp = props.cmp;
	const navigate = useNavigate();
	//For now, this /Admin page can only be accessed after login
	//Later, we should only use the role to access this page
	useEffect(() => {
		if (!localStorage.getItem("user-info")) {
			navigate("/");
		}
	}, []);

	return (
		<>
			<Cmp />
		</>
	);
}

export default AdminPage;

package dk.smartcyp.smirks;

public enum SMARTCYPReaction {
	Aliphatic_hydroxylation {
		@Override
		public String getSMIRKS() {
			return "[C;X4:1][H:2]>>[C:1][O][H:2]";
		}
	},
	Aromatic_hydroxylation {
		@Override
		public String getSMIRKS() {
			return "[c:1][H:2]>>[c:1][O][H:2]";
		}
	},

	O_dealkylation {
		@Override
		public String getSMIRKS() {
			return "[O:1][C:2]([H])>>[O:1][H].[C:2]=[O]";
		}
		@Override
		public String toString() {
			return "O-dealkylation";
		}
	},	

	Dioxolane_demethylation {
		@Override
		public String getSMIRKS() {
			return "[C:3]([#8:1])[#8:2]>>[#8:1].[#8:2].[C:3]=[O]";

			
		}

	},

	

	N_dealkylation {
		@Override
		public String getSMIRKS() {
			return "[#7:1][C:2]([H])>>[#7:1][H].[C:2]=[O]";
			
		}
		@Override
		public String toString() {
			return "N-dealkylation";
		}
	},
	S_oxidation {
		@Override
		public String getSMIRKS() {
			return "[#16:1] >> [#16:1](=[O])";
		}
		@Override
		public String toString() {
			return "S-oxidation";
		}
	},
	Thioesther_cleavage {
		@Override
		public String getSMIRKS() {
			return "[#16:1][C;X3:2] >>[#16:1][H].[C;X3:2][O]";
		}
	},
	N_oxidation {
		@Override
		public String getSMIRKS() {
			//"[N:1][C:2]([H])>>[N:1](-[O])[C:2]"
			//return "[N:1][C:2]>>[N+:1]([O-])[C:2]";
			//changed to #7 to cover isoquinoline
			return "[#7:1][#6:2]>>[#7+:1]([O-])[#6:2]";
			
		}
		@Override
		public String toString() {
			return "N-oxidation";
		}
	},
	Amine_hydroxylation {
		@Override
		public String getSMIRKS() {
			return "[N:1]([H:3])[#6:2]>>[N:1]([O][H:3])[#6:2]";
		}

	},
	Aldehyde_oxidation {
		@Override
		public String getSMIRKS() {
			return "[C;H1:1]=[O:2]>>[C:1](O)=[O:2]";
		}
	},
	Alcohol_oxidation {
		@Override
		public String getSMIRKS() {
			return "[C:1]([H])[O:2][H]>>[C:1]=[O:2]";
		}
	},
	Dihydropyrrole_aromatization {
		@Override
		public String getSMIRKS() {
			return "[N:1]1[C:2][C:3]=[C:4][C:5]1>>[N:1]1[C:2]=[C:3][C:4]=[C:5]1";
		}
	},
	Aromatization_of_dihydropyridines {
		@Override
		public String getSMIRKS() {
			return "[N;X3:1]1([H])[#6:2]=[#6:3][#6;X4:4]([H])[#6:5]=[#6:6]1>>[n;H0:1]1=[#6:2][#6:3]=[#6:4][#6:5]=[#6:6]1";
		}
	},
	Thioesther_bond_breaking {
		@Override
		public String getSMIRKS() {
			return "[S:1][C:2]=[O:3]>>[S:1][H].[C:2](O)=[O:3]";
		}
	},

	Desulphurization_of_phosphor {
		@Override
		public String getSMIRKS() {
			return "[*:1][P:2](=S)([*:3])[*:4]>>[*:1][P:2](=O)([*:3])[*:4]";
		}
	},
	Epoxidation {
		@Override
		public String getSMIRKS() {
			//"[C:1]=[C:2]>>[C:1]1=[C:2][O]1"
			return "[C:1]=[C:2]>>[C:1]1[C:2][O]1";
		}
	}
	;
	public abstract String getSMIRKS() ;
	@Override
	public String toString() {
		return name().replace("_", " ");
	}
}